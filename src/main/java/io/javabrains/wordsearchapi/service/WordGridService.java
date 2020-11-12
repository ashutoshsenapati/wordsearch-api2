package io.javabrains.wordsearchapi.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WordGridService {

       private class Coordinate{
            int x;
            int y;
            Coordinate(int x, int y){
                this.x = x;
                this.y = y;
            }
        }

    private enum Direction{
        HOR,
        VER,
        DIA,
        I_HOR,
        I_VER,
        I_DIA
    }

        public char[][] generateGrid(int GRID_SIZE, List<String> words){

           List<Coordinate> coordinates = new ArrayList<>();
            char [][]grid = new char[GRID_SIZE][GRID_SIZE];

            for(int i=0; i<grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    grid[i][j] = '_';
                    coordinates.add(new Coordinate(i,j));
                }
            }

            for(String word : words) {
                Collections.shuffle(coordinates);
                for(Coordinate coordinate : coordinates){
                    int x = coordinate.x;
                    int y = coordinate.y;
                    Direction selectedDirection = getFittingDirection(grid,word, coordinate);
                    if(selectedDirection!=null) {
                        switch(selectedDirection) {
                            case HOR:
                                for (char ch : word.toCharArray()) {
                                    grid[x][y++] = ch;
                                }
                                break;

                            case VER:
                                for (char ch : word.toCharArray()) {
                                    grid[x++][y] = ch;
                                }
                                break;

                            case DIA:
                                for (char ch : word.toCharArray()) {
                                    grid[x++][y++] = ch;
                                }
                                break;

                            case I_VER:
                                for (char ch : word.toCharArray()) {
                                    grid[x--][y] = ch;
                                }
                                break;
                            case I_HOR:
                                for (char ch : word.toCharArray()) {
                                    grid[x][y--] = ch;
                                }
                                break;

                            case I_DIA:
                                for (char ch : word.toCharArray()) {
                                    grid[x--][y--] = ch;
                                }
                                break;
                        }
                        break;
                    }
                }
            }
            randomFill(grid);
            return grid;
        }

        public void displayGrid(char[][]grid){
            for(int i=0; i<grid.length; i++){
                for(int j=0; j<grid[0].length; j++){
                    System.out.print(grid[i][j] + " ");
                }
                System.out.println();
            }
        }

        private void randomFill(char[][]grid){
            String allCaps = "AABCDEFGHIJKLMNOPQRSTUVWXYZ";

            for(int i=0; i<grid.length; i++){
                for(int j=0; j<grid[0].length; j++){
                    if(grid[i][j] == '_'){
                        int randomIndex = ThreadLocalRandom.current().nextInt(0,allCaps.length());
                        grid[i][j] = allCaps.charAt(randomIndex);
                    }
                }
            }
        }
        private Direction getFittingDirection(char[][]grid,String word, Coordinate coordinate){

            List<Direction> directions = Arrays.asList(Direction.values());
            Collections.shuffle(directions);

            for(Direction direction : directions){
                if(wordFits(grid,word, coordinate,direction))
                    return direction;
            }
            return null;
        }

        private boolean wordFits(char[][]grid, String word, Coordinate coordinate, Direction direction){
            int GRID_SIZE = grid.length;
            switch(direction){
                case HOR:
                    if (coordinate.y + word.length() > GRID_SIZE)
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (grid[coordinate.x][coordinate.y+i] != '_' && grid[coordinate.x][coordinate.y+i]!=word.charAt(i))
                            return false;
                    }
                    break;

                case VER:
                    if (coordinate.x + word.length() > GRID_SIZE)
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (grid[coordinate.x + i][coordinate.y] != '_' && grid[coordinate.x + i][coordinate.y]!=word.charAt(i))
                            return false;
                    }
                    break;

                case DIA:
                    if (coordinate.y + word.length() > GRID_SIZE || coordinate.x + word.length() > GRID_SIZE )
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (grid[coordinate.x+i][coordinate.y + i] != '_' && grid[coordinate.x + i][coordinate.y+1]!=word.charAt(i))
                            return false;
                    }
                    break;

                case I_VER:
                    if (coordinate.x < word.length() )
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (grid[coordinate.x-i][coordinate.y] != '_' && grid[coordinate.x-i][coordinate.y] != word.charAt(i))
                            return false;
                    }
                    break;
                case I_HOR:
                    if (coordinate.y < word.length())
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (grid[coordinate.x][coordinate.y-i] != '_' && grid[coordinate.x][coordinate.y-i] != word.charAt(i))
                            return false;
                    }
                    break;
                case I_DIA:
                    if (coordinate.y < word.length() || coordinate.x < word.length())
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (grid[coordinate.x-i][coordinate.y-i] != '_' && grid[coordinate.x-i][coordinate.y-i] !=word.charAt(i))
                            return false;
                    }
                    break;
            }
            return true;
        }
    }
