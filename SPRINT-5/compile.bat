@echo off


@echo off
echo ========================================
echo   COMPILATION DU FRAMEWORK
echo ========================================

REM 🔥 CONFIGURATION DES CHEMINS
set FRAMEWORK_DIR=framework
set TEST_DIR=test\src\main\webapp\WEB-INF\lib

echo [1/2] Construction du projet...
cd "%FRAMEWORK_DIR%"
call mvn clean package

if errorlevel 1 (
    echo ❌ ERREUR: La construction a échoué!
    pause
    exit /b 1
)
echo ✅ Construction réussie!

echo [2/2] Copie vers le dossier test...
cd ..

if not exist "%TEST_DIR%" (
    echo 📁 Création du dossier: %TEST_DIR%
    mkdir "%TEST_DIR%"
)

if exist "%FRAMEWORK_DIR%\target\sprint5-core.jar" (
    copy /Y "%FRAMEWORK_DIR%\target\sprint5-core.jar" "%TEST_DIR%"
    echo ✅ Fichiers copiés vers: %TEST_DIR%
) else (
    echo ❌ Fichiers non trouvés dans %FRAMEWORK_DIR%\target\
    dir "%FRAMEWORK_DIR%\target\"
    pause
    exit /b 1
)



@echo off
title Compilation et Déploiement MonFramework
echo ===============================================
echo  COMPILATION ET DEPLOIEMENT MONFRAMEWORK
echo ===============================================

REM --- CONFIGURATION - MODIFIEZ CE CHEMIN ---
set TOMCAT_WEBAPPS="C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps"
REM ------------------------------------------

echo Chemin Tomcat: %TOMCAT_WEBAPPS%

REM Vérifier si le dossier Tomcat existe
if not exist %TOMCAT_WEBAPPS% (
    echo ❌ ERREUR: Dossier Tomcat non trouve: %TOMCAT_WEBAPPS%
    echo Veuillez modifier le chemin dans le fichier BAT
    pause
    exit /b 1
)

REM Se positionner dans le dossier test
cd /d "C:\Users\Zed\Documents\GitHub\Projet_Sprint5\test"

if errorlevel 1 (
    echo ❌ ERREUR: Impossible d'acceder au dossier test
    echo Verifiez le chemin: C:\Users\Zed\Documents\GitHub\Projet_Sprint5\test
    pause
    exit /b 1
)

echo [1/3] Nettoyage et compilation...
call mvn clean package

if errorlevel 1 (
    echo ❌ ERREUR: Echec de la compilation Maven
    echo Verifiez la configuration Maven et les dependances
    pause
    exit /b 1
)

echo ✅ Compilation Maven reussie!

echo [2/3] Verification du WAR...
set WAR_FILE=target\sprint5-core.war

if not exist "%WAR_FILE%" (
    echo ❌ ERREUR: Fichier WAR non genere: %WAR_FILE%
    echo Contenu du dossier target:
    dir target\
    pause
    exit /b 1
)

echo ✅ Fichier WAR trouve: %WAR_FILE%

echo [3/3] Copie vers Tomcat...
echo Copie de %WAR_FILE% vers %TOMCAT_WEBAPPS%...
copy "%WAR_FILE%" %TOMCAT_WEBAPPS%

if errorlevel 1 (
    echo ❌ ERREUR: Echec de la copie vers Tomcat
    echo Verifiez les permissions d'ecriture
    pause
    exit /b 1
)

echo ✅ Copie reussie!
echo.
echo ===============================================
echo  DEPLOIEMENT TERMINE AVEC SUCCES!
echo ===============================================
echo Le fichier monframework.war a ete deploye dans:
echo %TOMCAT_WEBAPPS%
echo.
echo Redemarrez Tomcat pour appliquer les changements.
pause
echo.
echo ===============================================
echo  SUCCES! DEPLOIEMENT TERMINE
echo ===============================================
echo.
echo Fichier: test-monframework.war
echo URL: http://localhost:8080/monframework/
echo.
echo N'oubliez pas de redemarrer Tomcat!
pause