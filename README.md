# dart-ng2-spring-maven-plugin
Compiles your Dart + Angular2 sources!

## Usage
- Clone sources
- `mvn clean install`
- Put your Dart+Ng2 sources to (a subfolder of) src/main/dart and you're ready to go.

### Maven config
```
<build>
        <plugins>
            <plugin>
                <groupId>net.k40s.nico</groupId>
                <artifactId>dart-ng2-spring-maven-plugin</artifactId>
                <version>1.0</version>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>run</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

So every time you run `mvn clean compile` in your dart + spring project, maven runs this plugin which compiles your sources and copies them to `src/main/resources/static` by default.
