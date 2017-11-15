local juego = ARGV[1]
local letraABuscar = ARGV[2]
print(juego)
print(letraABuscar)
local llaveWord = ("word:" .. juego)
local llaveGuess = ("guessedWord:" .. juego)
local longitud = redis.call('LLEN', llaveWord)
local contador = 0
for i=0,longitud-1 
do 
	if redis.call('lrange', llaveWord,i,i)[1] == letraABuscar then
		redis.call('LSET', llaveGuess, i,letraABuscar)
		
	else
		contador = contador + 1
	end
end


local palabraRetornar = ""
for i=0,longitud-1 
do 
	palabraRetornar = (palabraRetornar .. redis.call('lrange',llaveGuess,i,i)[1])
end


return palabraRetornar
