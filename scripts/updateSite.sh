mvn clean site
mvn site:stage
scp -r target/site/* pawc.ddns.net:/var/www/html/Chat/
scp -r ChatServer/target/site/* pawc.ddns.net:/var/www/html/Chat/ChatServer
scp -r ChatClient/target/site/* pawc.ddns.net:/var/www/html/Chat/ChatClient
