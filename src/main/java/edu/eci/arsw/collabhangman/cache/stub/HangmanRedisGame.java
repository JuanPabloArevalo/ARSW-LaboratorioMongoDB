/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.cache.stub;

import edu.eci.arsw.collabhangman.model.game.HangmanGame;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 *
 * @author 2087559
 */
public class HangmanRedisGame extends HangmanGame {


    public String identificadorPartida;
    private StringRedisTemplate template;
    public static String palabra;

    public HangmanRedisGame(String word, StringRedisTemplate template, String identificadorPartida) {
        super(word);
        this.identificadorPartida = "game:" + identificadorPartida;
        this.template = template;
    }

    /**
     *
     * @param template
     * @param identificadorPartida
     */
    public HangmanRedisGame(StringRedisTemplate template, String identificadorPartida) {
        super("");
        this.identificadorPartida = "game:" + identificadorPartida;
        this.template = template;
    }

//    
//    @Autowired
//    RedisScript<String> script = null;
//    
//    @Bean
//    public RedisScript<String> script() {
//        System.out.println ("1");
//        DefaultRedisScript<String> redisScript = new DefaultRedisScript<String>();
//        System.out.println ("2");
//        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/script.lua")));
//        System.out.println ("3");
//        redisScript.setResultType(String.class);
//        System.out.println ("4");
//        return redisScript;
//    }
    

    @Override
    public String addLetter(char l) throws RedisCacheException {
//        try {
//            System.out.println (template.execute(script,  Collections.emptyList(), identificadorPartida, l));
//            template.execute(script, keys, args)new SessionCallback<List< Object>>() {
//                @Override
//                public <T> execute(RedisOperations<K, V> ro) throws DataAccessException {
//                    ro.watch((K) identificadorPartida);
//                    ro.multi();
//                    System.out.println("Palabra:"+l);
//                    System.out.println("Id:"+identificadorPartida);
//                    return ro.execute(script, Collections.emptyList(), identificadorPartida, l);
//                }
//            });
//            return "";
//                return palabra;
//            }catch (RedisConnectionFailureException e) {
//                throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
//            }
        try {
            String wordS = (String) template.opsForHash().get(identificadorPartida, "word");
            wordS = wordS.toLowerCase();
            String guessedWord2 = (String) template.opsForHash().get(identificadorPartida, "guessedWord");

            this.guessedWord = new char[guessedWord2.length()];
            for (int i = 0; i < guessedWord2.length(); i++) {
                this.guessedWord[i] = guessedWord2.charAt(i);
            }

            for (int i = 0; i < wordS.length(); i++) {
                if (wordS.charAt(i) == l) {
                    this.guessedWord[i] = l;
                }
            }
            guessedWord2 = "";
            for (int i = 0; i < this.guessedWord.length; i++) {
                guessedWord2 = guessedWord2 + this.guessedWord[i];
            }
            template.opsForHash().put(identificadorPartida, "guessedWord", guessedWord2);
            return new String(this.guessedWord);
        } catch (RedisConnectionFailureException e) {
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        }

    }

    @Override
    public boolean tryWord(String playerName, String s) throws RedisCacheException {
        try {
            String wordS = (String) template.opsForHash().get(identificadorPartida, "word");
            if (s.toLowerCase().equals(wordS)) {
                winner = playerName;
                gameFinished = true;
                guessedWord = wordS.toCharArray();
                template.opsForHash().put(identificadorPartida, "winner", playerName);
                template.opsForHash().put(identificadorPartida, "gameFinished", "S");
                template.opsForHash().put(identificadorPartida, "guessedWord", s);
                return true;
            }
            return false;
        } catch (RedisConnectionFailureException e) {
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        }
    }

    @Override
    public boolean gameFinished() throws RedisCacheException {
        try {
            String finalizado = (String) template.opsForHash().get(identificadorPartida, "gameFinished");
            return "S".equalsIgnoreCase(finalizado);
        } catch (RedisConnectionFailureException e) {
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        }
    }

    /**
     * @pre gameFinished=true;
     * @return winner's name
     */
    @Override
    public String getWinnerName() {
        return (String) template.opsForHash().get(identificadorPartida, "winner");
    }

    @Override
    public String getCurrentGuessedWord() throws RedisCacheException {
        try {
            String a = (String) template.opsForHash().get(identificadorPartida, "guessedWord");
            if (a == null) {
                throw new RedisCacheException("El identificador ingresado no existe.");
            } else {
                return a;
            }
        } catch (RedisConnectionFailureException e) {
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        } catch (Exception e) {
            throw new RedisCacheException(e.getMessage());
        }
    }

}
