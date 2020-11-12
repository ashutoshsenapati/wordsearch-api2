package io.javabrains.wordsearchapi.controllers;

import io.javabrains.wordsearchapi.service.WordGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController("/")
public class WordSearchController {

    @Autowired
    WordGridService wordGridService;

    @GetMapping("/wordgrid")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:1234")
    public String createWorGrid(@RequestParam int gridSize, @RequestParam String wordList){
        List<String> words  = Arrays.asList(wordList.split(","));
             char[][] grid = wordGridService.generateGrid(gridSize,words );

             String gridToString = "";

        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid[0].length; j++){
                gridToString +=grid[i][j] + " ";
            }
            gridToString+="\r\n";
        }
        return gridToString;
        }
    }
