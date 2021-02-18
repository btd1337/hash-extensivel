# Hash Extensível

## Instruções

### Linha de Comando

#### Compilar o projeto

- Entre na pasta do projeto pelo terminal
- Execute os comandos:

```sh
javac -d . src/**/*.java
jar cvmf META-INF/MANIFEST.MF ed2.jar ed2
```

> Caso apareça a mensagem abaixo apenas ignore

```
Note: Some input files use unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
```

#### Executar

- Após ter compilado o projeto, ainda dentro da pasta do projeto, pelo terminal execute:

```sh
java -jar ed2.jar
```
