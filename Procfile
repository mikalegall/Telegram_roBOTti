web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/*.jar --port $PORT -Dfile.encoding=UTF-8 -cp target/classes/:target/dependency
worker: java $JAVA_OPTS -jar target/*.jar