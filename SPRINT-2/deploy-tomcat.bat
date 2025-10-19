@echo off
REM ========================================
REM Script de déploiement automatisé
REM MVC Framework - Sprint 1
REM ========================================

setlocal enabledelayedexpansion

REM Configuration
set PROJECT_NAME=mvc-test-project
set WAR_NAME=mvc-test-project.war
set TOMCAT_WEBAPPS=C:\apache-tomcat-10.1.28\webapps
set DEPLOY_DIR=%TOMCAT_WEBAPPS%\mvc-test
set BUILD_PROFILE=development

echo.
echo ========================================
echo   MVC Framework - Déploiement Automatisé
echo ========================================
echo.

REM Vérification de l'environnement
echo [1/6] Vérification de l'environnement...
where mvn >nul 2>&1
if errorlevel 1 (
    echo ERREUR: Maven n'est pas installé ou pas dans le PATH
    pause
    exit /b 1
)

if not exist "%TOMCAT_WEBAPPS%" (
    echo ERREUR: Répertoire Tomcat webapps non trouvé: %TOMCAT_WEBAPPS%
    echo Veuillez modifier la variable TOMCAT_WEBAPPS dans ce script
    pause
    exit /b 1
)

echo ✓ Maven trouvé
echo ✓ Répertoire Tomcat trouvé

REM Nettoyage des anciens builds
echo.
echo [2/6] Nettoyage des anciens builds...
call mvn clean -q
if errorlevel 1 (
    echo ERREUR: Échec du nettoyage Maven
    pause
    exit /b 1
)
echo ✓ Nettoyage terminé

REM Compilation et packaging
echo.
echo [3/6] Compilation et packaging...
echo Profil actif: %BUILD_PROFILE%
call mvn package -P%BUILD_PROFILE% -DskipTests=false
if errorlevel 1 (
    echo ERREUR: Échec de la compilation/packaging
    pause
    exit /b 1
)
echo ✓ Compilation et packaging terminés

REM Vérification du WAR généré
echo.
echo [4/6] Vérification du fichier WAR...
set WAR_PATH=test-project\target\%WAR_NAME%
if not exist "%WAR_PATH%" (
    echo ERREUR: Fichier WAR non trouvé: %WAR_PATH%
    pause
    exit /b 1
)
echo ✓ Fichier WAR trouvé: %WAR_PATH%

REM Arrêt de l'ancienne application (si elle existe)
echo.
echo [5/6] Préparation du déploiement...
if exist "%DEPLOY_DIR%" (
    echo Suppression de l'ancienne application...
    rmdir /s /q "%DEPLOY_DIR%" 2>nul
    echo ✓ Ancienne application supprimée
)

if exist "%TOMCAT_WEBAPPS%\%WAR_NAME%" (
    echo Suppression de l'ancien WAR...
    del "%TOMCAT_WEBAPPS%\%WAR_NAME%" 2>nul
    echo ✓ Ancien WAR supprimé
)

REM Déploiement du nouveau WAR
echo.
echo [6/6] Déploiement de la nouvelle application...
copy "%WAR_PATH%" "%TOMCAT_WEBAPPS%\" >nul
if errorlevel 1 (
    echo ERREUR: Échec de la copie du WAR
    pause
    exit /b 1
)
echo ✓ WAR copié vers Tomcat

REM Attendre le déploiement automatique
echo.
echo Attente du déploiement automatique de Tomcat...
timeout /t 5 /nobreak >nul

REM Vérification du déploiement
if exist "%DEPLOY_DIR%" (
    echo ✓ Application déployée avec succès
) else (
    echo ⚠ Application en cours de déploiement...
)

REM Affichage des informations finales
echo.
echo ========================================
echo   DÉPLOIEMENT TERMINÉ
echo ========================================
echo.
echo 📁 Répertoire de déploiement: %DEPLOY_DIR%
echo 🌐 URL d'accès: http://localhost:8080/mvc-test/
echo 📊 URL de debug: http://localhost:8080/mvc-test/debug
echo 📝 Logs Tomcat: %TOMCAT_WEBAPPS%\..\logs\
echo.
echo 🚀 Pour démarrer Tomcat (si pas déjà fait):
echo    cd %TOMCAT_WEBAPPS%\..\bin
echo    startup.bat
echo.
echo 🛑 Pour arrêter Tomcat:
echo    cd %TOMCAT_WEBAPPS%\..\bin
echo    shutdown.bat
echo.

REM Option pour ouvrir le navigateur
set /p OPEN_BROWSER="Ouvrir l'application dans le navigateur ? (o/N): "
if /i "%OPEN_BROWSER%"=="o" (
    start http://localhost:8080/mvc-test/
)

echo.
echo Appuyez sur une touche pour continuer...
pause >nul

endlocal