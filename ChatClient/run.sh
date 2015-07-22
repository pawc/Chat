cp src/main/java/pawc/chat/client/Main.fxml src/main/resources/pawc/chat/client/Main.fxml 
cp src/main/java/pawc/chat/client/About.fxml src/main/resources/pawc/chat/client/About.fxml


cp src/main/java/pawc/chat/client/controller/Settings.fxml src/main/resources/pawc/chat/client/controller/Settings.fxml 
mvn clean package
cd target
java -jar ChatClient-1.0-SNAPSHOT-jar-with-dependencies.jar 

