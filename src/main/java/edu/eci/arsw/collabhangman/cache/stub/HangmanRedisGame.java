/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.cache.stub;

import edu.eci.arsw.collabhangman.model.game.HangmanGame;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

/**
 *
 * @author 2087559
 */
public class HangmanRedisGame extends HangmanGame{
    public String identificadorPartida;
    private StringRedisTemplate template;
    
    
    public HangmanRedisGame(String word, StringRedisTemplate template, String identificadorPartida) {
        super(word);
        this.identificadorPartida="game:"+identificadorPartida;
        this.template=template;
    }
    
    /**
     *
     * @param template
     * @param identificadorPartida
     */
    public HangmanRedisGame(StringRedisTemplate template, String identificadorPartida) {
        super("");
        this.identificadorPartida="game:"+identificadorPartida;
        this.template=template;
    }

    @Override
    public String addLetter(char l) throws RedisCacheException{   
        try{
            String wordS = (String)template.opsForHash().get(identificadorPartida, "word");
            wordS = wordS.toLowerCase();

            String guessedWord2 = (String)template.opsForHash().get(identificadorPartida, "guessedWord"); 

            this.guessedWord = new char[guessedWord2.length()];
            for (int i=0;i<guessedWord2.length();i++){
                this.guessedWord[i]=guessedWord2.charAt(i);
            } 

            for (int i=0;i<wordS.length();i++){
                if (wordS.charAt(i)==l){
                    this.guessedWord[i]=l;
                }            
            }    
            guessedWord2 = "";
            for(int i=0; i<this.guessedWord.length; i++){
                 guessedWord2 = guessedWord2+this.guessedWord[i];
            }
            template.opsForHash().put(identificadorPartida,"guessedWord",guessedWord2);
            return new String(this.guessedWord);
        }catch(RedisConnectionFailureException e){
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        }
        
    }
    
    @Override
    public boolean tryWord(String playerName,String s) throws RedisCacheException{
        try{
            String wordS = (String)template.opsForHash().get(identificadorPartida, "word"); 
            if (s.toLowerCase().equals(wordS)){
                winner=playerName;
                gameFinished=true;
                guessedWord=wordS.toCharArray();
                template.opsForHash().put(identificadorPartida,"winner",playerName);
                template.opsForHash().put(identificadorPartida,"gameFinished","S");
                template.opsForHash().put(identificadorPartida,"guessedWord",s);
                return true;
            }
            return false;
        }catch(RedisConnectionFailureException e){
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        }
    }
    
    @Override
    public boolean gameFinished() throws RedisCacheException{
        try{
            String finalizado = (String)template.opsForHash().get(identificadorPartida, "gameFinished"); 
            return "S".equalsIgnoreCase(finalizado);
        }catch(RedisConnectionFailureException e){
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        }
    }
    
    /**
     * @pre gameFinished=true;
     * @return winner's name
     */
    @Override
    public String getWinnerName(){
        return (String)template.opsForHash().get(identificadorPartida, "winner");
    }
    
    @Override
    public String getCurrentGuessedWord() throws RedisCacheException{
        try{
            String a = (String)template.opsForHash().get(identificadorPartida, "guessedWord");
            if(a==null){
                throw new RedisCacheException("El identificador ingresado no existe.");
            }
            else{
               return a; 
            }
        }catch(RedisConnectionFailureException e){
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        }catch(Exception e){
            throw new RedisCacheException(e.getMessage());
        }
    }  
    
}
