@echo off
echo === Deploiement MiniFrameworkServlet ===
setlocal

:: ---------------------------
:: 1. Definir le chemin de Tomcat
:: ---------------------------
set "TOMCAT_PATH=C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps"

:: Verifier que Tomcat existe
if not exist "%TOMCAT_PATH%" (
    echo ERREUR: Le repertoire Tomcat n'existe pas: %TOMCAT_PATH%
    echo Veuillez verifier le chemin de Tomcat.
    pause
    exit /b 1
)

:: ---------------------------
:: 2. Compiler le Framework
:: ---------------------------
echo.
echo Compilation du Framework...
if not exist "Framework" (
    echo ERREUR: Le repertoire Framework n'existe pas
    pause
    exit /b 1
)

cd Framework
mvn clean install
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Erreur lors de la compilation du Framework
    cd ..
    pause
    exit /b %ERRORLEVEL%
)
cd ..

:: ---------------------------
:: 3. Verifier que le JAR du Framework a ete cree
:: ---------------------------
if not exist "Framework\target\Framework-1.0-SNAPSHOT.jar" (
    echo ERREUR: Le JAR du Framework n'a pas ete genere
    pause
    exit /b 1
)

:: ---------------------------
:: 4. Copier le jar dans Test/lib
:: ---------------------------
echo.
echo Copie du JAR Framework dans Test/lib...
if not exist "Test\lib" mkdir "Test\lib"
copy /Y "Framework\target\Framework-1.0-SNAPSHOT.jar" "Test\lib\Framework-1.0-SNAPSHOT.jar"
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Impossible de copier le JAR du Framework
    pause
    exit /b %ERRORLEVEL%
)

:: ---------------------------
:: 5. Compiler Test pour generer le WAR
:: ---------------------------
echo.
echo Compilation du Test...
if not exist "Test" (
    echo ERREUR: Le repertoire Test n'existe pas
    pause
    exit /b 1
)

cd Test
mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Erreur lors de la compilation du Test
    cd ..
    pause
    exit /b %ERRORLEVEL%
)

:: ---------------------------
:: 6. Detecter le WAR genere
:: ---------------------------
set "WAR_FILE="
if exist "target\Test.war" (
    set "WAR_FILE=target\Test.war"
) else if exist "target\Test-1.0-SNAPSHOT.war" (
    set "WAR_FILE=target\Test-1.0-SNAPSHOT.war"
) else (
    echo ERREUR: Aucun fichier WAR trouve dans Test\target\
    cd ..
    pause
    exit /b 1
)

echo WAR detecte: %WAR_FILE%
cd ..

:: ---------------------------
:: 7. Supprimer l'ancien deploiement
:: ---------------------------
if exist "%TOMCAT_PATH%\Test" (
    echo Suppression de l'ancien deploiement...
    rmdir /s /q "%TOMCAT_PATH%\Test"
)
if exist "%TOMCAT_PATH%\Test.war" (
    del /q "%TOMCAT_PATH%\Test.war"
)

:: ---------------------------
:: 8. Copier le WAR dans Tomcat/webapps
:: ---------------------------
echo.
echo Copie du WAR dans Tomcat...
copy /Y "Test\%WAR_FILE%" "%TOMCAT_PATH%\Test.war"
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Impossible de copier le WAR dans Tomcat
    pause
    exit /b %ERRORLEVEL%
)

