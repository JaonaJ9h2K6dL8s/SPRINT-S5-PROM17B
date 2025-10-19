@echo off
REM ========================================
REM Script de démarrage du serveur de développement
REM MVC Framework - Sprint 1
REM ========================================

setlocal enabledelayedexpansion

echo.
echo ========================================
echo   MVC Framework - Serveur de Développement
echo ========================================
echo.

REM Vérification de Maven
echo [1/3] Vérification de l'environnement...
where mvn >nul 2>&1
if errorlevel 1 (
    echo ERREUR: Maven n'est pas installé ou pas dans le PATH
    pause
    exit /b 1
)
echo ✓ Maven trouvé

REM Compilation rapide
echo.
echo [2/3] Compilation du projet...
call mvn compile -q -f test-project\pom.xml
if errorlevel 1 (
    echo ERREUR: Échec de la compilation
    pause
    exit /b 1
)
echo ✓ Compilation terminée

REM Démarrage du serveur
echo.
echo [3/3] Démarrage du serveur de développement...
echo.
echo 🚀 Serveur Tomcat intégré en cours de démarrage...
echo 🌐 URL d'accès: http://localhost:8080/mvc-test/
echo 🔧 Mode: Développement
echo.
echo Pour arrêter le serveur, appuyez sur Ctrl+C
echo.

cd test-project
call mvn tomcat7:run -Pdevelopment

echo.
echo Serveur arrêté.
pause

endlocal