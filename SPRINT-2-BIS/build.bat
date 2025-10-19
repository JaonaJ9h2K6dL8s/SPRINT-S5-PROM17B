@echo off
echo ==========================================
echo    Framework Sprint 2-bis - Build Script
echo ==========================================

set APP_NAME=framework-sprint2bis-test
set SRC_DIR=src\main\java
set WEB_DIR=src\main\webapp
set BUILD_DIR=build
set LIB_DIR=lib
set CLASSES_DIR=%BUILD_DIR%\WEB-INF\classes
set FRAMEWORK_JAR=jframework-sprint2bis.jar

REM Nettoyage des anciens builds
echo [1/7] Nettoyage des anciens builds...
if exist %BUILD_DIR% rmdir /s /q %BUILD_DIR%
if exist %FRAMEWORK_JAR% del %FRAMEWORK_JAR%
if exist sources.txt del sources.txt

REM Création des répertoires de build
echo [2/7] Création des répertoires de build...
mkdir %CLASSES_DIR%
mkdir %BUILD_DIR%\WEB-INF\lib

REM Copie des bibliothèques
echo [3/7] Copie des bibliothèques...
xcopy "%LIB_DIR%\*" "%BUILD_DIR%\WEB-INF\lib\" /Y

REM Copie des fichiers web (JSP, HTML, CSS, etc.)
echo [4/7] Copie des fichiers web...
xcopy "%WEB_DIR%\*" "%BUILD_DIR%\" /E /Y

REM Compilation des sources Java
echo [5/7] Compilation des sources Java...
dir /s /b %SRC_DIR%\*.java > sources.txt

REM Vérification de l'existence de javac
javac -version >nul 2>&1
if errorlevel 1 (
    echo ERREUR: javac n'est pas trouvé dans le PATH
    echo Veuillez installer le JDK et configurer la variable PATH
    pause
    exit /b 1
)

REM Compilation avec toutes les dépendances
echo Compilation en cours...
javac -cp "%LIB_DIR%\*" -d "%CLASSES_DIR%" @sources.txt
if errorlevel 1 (
    echo ERREUR: Échec de la compilation
    echo Vérifiez les erreurs ci-dessus
    pause
    exit /b 1
)

REM Création du JAR du framework
echo [6/7] Création du JAR du framework...
cd %CLASSES_DIR%
jar cf ..\..\..\%FRAMEWORK_JAR% jframework\*
cd ..\..\..

REM Création du WAR de l'application de test
echo [7/7] Création du WAR de l'application de test...
cd %BUILD_DIR%
jar cf ..\%APP_NAME%.war *
cd ..

REM Nettoyage des fichiers temporaires
del sources.txt

echo.
echo ==========================================
echo           BUILD TERMINÉ AVEC SUCCÈS
echo ==========================================
echo.
echo Fichiers générés :
echo   - %FRAMEWORK_JAR% (Framework JAR)
echo   - %APP_NAME%.war (Application WAR)
echo.
echo Fonctionnalités Sprint 2-bis :
echo   ✓ Auto-découverte des contrôleurs (@Controller)
echo   ✓ Routage intelligent (@Url)
echo   ✓ Support multi-packages
echo   ✓ Interface utilisateur améliorée
echo   ✓ Compatibilité ascendante
echo.
echo Pour déployer : Copiez le fichier %APP_NAME%.war
echo dans le répertoire webapps de votre serveur Tomcat
echo.
pause