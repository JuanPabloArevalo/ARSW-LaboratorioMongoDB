/*
 * Copyright (C) 2016 Pivotal Software, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.arsw.collabhangman.model.game;

import edu.eci.arsw.collabhangman.cache.stub.RedisCacheException;

/**
 *
 * @author hcadavid
 */
public class HangmanGame {
    
    protected final String word;
    protected char[] guessedWord;
    protected String winner="";
    protected boolean gameFinished=false;

    public HangmanGame(String word) {
        this.word=word.toLowerCase();
        guessedWord=new char[word.length()];
        for (int i=0;i<word.length();i++){
            guessedWord[i]='_';
        }                
    }
    
    /**
     * @pre gameFinished==false
     * @param l new letter
     * @return the secret word with all the characters 'l' revealed
     */
    public String addLetter(char l) throws RedisCacheException{                
        for (int i=0;i<word.length();i++){
            if (word.charAt(i)==l){
                guessedWord[i]=l;
            }            
        }    
        return new String(guessedWord);
    }
    
    public boolean tryWord(String playerName,String s) throws RedisCacheException{
        if (s.toLowerCase().equals(word)){
            winner=playerName;
            gameFinished=true;
            guessedWord=word.toCharArray();
            return true;
        }
        return false;
    }
    
    public boolean gameFinished() throws RedisCacheException{
        return gameFinished;
    }
    
    /**
     * @pre gameFinished=true;
     * @return winner's name
     */
    public String getWinnerName(){
        return winner;
    }
    
    public String getCurrentGuessedWord() throws RedisCacheException{
        return new String(guessedWord);
    }    
    
}
