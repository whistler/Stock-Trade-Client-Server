all:
	javac -d target -cp lib/ormlite-core-4.45.jar:lib/ormlite-jdbc-4.45.jar:lib/sqlite-jdbc-3.7.2.jar  src/main/java/Owns.java src/main/java/PriceUpdateApi.java src/main/java/PriceUpdateClient.java src/main/java/Stock.java src/main/java/StockUpdater.java src/main/java/TradeApi.java src/main/java/TradeClient.java src/main/java/TradeServer.java src/main/java/User.java

server:
	java -cp target:lib/ormlite-core-4.45.jar:lib/ormlite-jdbc-4.45.jar:lib/sqlite-jdbc-3.7.2.jar TradeServer

tradeclient:
	java -cp target:lib/ormlite-core-4.45.jar:lib/ormlite-jdbc-4.45.jar:lib/sqlite-jdbc-3.7.2.jar TradeClient

priceupdateclient:
	java -cp target:lib/ormlite-core-4.45.jar:lib/ormlite-jdbc-4.45.jar:lib/sqlite-jdbc-3.7.2.jar PriceUpdateClient

clean:
	rm target/*.class