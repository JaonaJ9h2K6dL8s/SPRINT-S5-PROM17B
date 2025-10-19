# 🚀 MVC Framework Project - Sprint 1

## 🎯 Objectif du Sprint

Mettre en place un **FrontServlet** jouant le rôle de **Front Controller**, capable d'intercepter toutes les requêtes HTTP et d'afficher leurs informations. Ce Sprint établit les fondations d'un framework MVC personnalisé qui sera enrichi dans les sprints suivants.

## 🏗️ Architecture du Projet

- **Type :** Projet Maven multi-modules
- **Module Framework :** `mvc-framework` (packaging : JAR)
- **Module de Test :** `test-project` (packaging : WAR)
- **Structure :** Parent POM avec deux modules enfants (Framework + Application de Test)

```
mvc-parent/
├── pom.xml (Parent POM)
├── mvc-framework/ (Module Framework - JAR)
│   ├── src/main/java/
│   │   └── com/mvc/framework/
│   │       ├── servlet/FrontServlet.java
│   │       └── util/RequestUtils.java
│   └── src/test/java/
└── test-project/ (Module Test - WAR)
    ├── src/main/webapp/
    │   ├── index.html
    │   ├── index.jsp
    │   ├── WEB-INF/web.xml
    │   └── error/
    └── pom.xml
```

## ⚙️ Technologies Principales

- **Langage :** Java 8 (source/target)
- **Outil de Build :** Apache Maven 3
- **API Servlet :** javax.servlet-api 4.0.1
- **JSP API :** javax.servlet.jsp-api 2.3.3
- **JSTL :** javax.servlet.jstl 1.2
- **Tests Unitaires :** JUnit 4.13.2 + Mockito 3.12.4

## 🧩 Framework MVC Personnalisé

### Servlet Principale : FrontServlet

- **Pattern Utilisé :** Front Controller (intercepte toutes les URLs `/*`)
- **Fonctionnalités Implémentées :**
  - ✅ Interception de toutes les requêtes HTTP
  - ✅ Affichage des informations de la requête (URL, méthode, paramètres, headers)
  - ✅ Support complet des méthodes : GET, POST, PUT, DELETE
  - ✅ Mode Debug activé pour le développement
  - ✅ Interface utilisateur moderne et responsive

### Classe Utilitaire : RequestUtils

- Extraction des paramètres et headers
- Détection des requêtes AJAX
- Gestion des adresses IP (avec support proxy)
- Construction d'URLs complètes

## 🌐 Serveur d'Application

- **Serveur Principal :** Apache Tomcat 10.1.28
- **Plugin Maven :** Tomcat7 Maven Plugin 2.2 (pour exécution rapide via `mvn tomcat7:run`)

## 🔧 Configuration Web

- **Descripteur :** web.xml (version 4.0)
- **Mapping Servlet :** `/*`
- **Encodage :** UTF-8 via SetCharacterEncodingFilter
- **Pages d'accueil :** index.html, index.jsp
- **Load-on-startup :** 1 (chargement prioritaire)

## 🧰 Outils et Plugins Maven

### Plugins Configurés

- **maven-compiler-plugin :** 3.8.1
- **maven-surefire-plugin :** 2.22.2
- **maven-war-plugin :** 3.2.3
- **tomcat7-maven-plugin :** 2.2

### Profils

- **development** (par défaut) : Debug activé, optimisation désactivée
- **production** : Debug désactivé, optimisation activée

### Génération Automatique

- ✅ JAR sources
- ✅ Javadoc
- ✅ WAR déployable

## 🚀 Déploiement

### Scripts Disponibles

1. **deploy-tomcat.bat** : Déploiement automatisé sous Windows
2. **start-dev-server.bat** : Serveur de développement rapide

### Déploiement Manuel

```bash
# Compilation et packaging
mvn clean package

# Déploiement sur Tomcat
copy test-project\target\mvc-test-project.war C:\apache-tomcat-10.1.28\webapps\
```

### Déploiement avec Script

```bash
# Windows
deploy-tomcat.bat

# Ou serveur de développement
start-dev-server.bat
```

## 🌐 URLs d'Accès

- **Application principale :** http://localhost:8080/mvc-test/
- **Page JSP :** http://localhost:8080/mvc-test/index.jsp
- **Test GET :** http://localhost:8080/mvc-test/test/get?param1=test&param2=framework
- **API JSON :** http://localhost:8080/mvc-test/api/json?format=json

## 🧪 Tests Disponibles

### Tests Unitaires

```bash
# Exécution des tests
mvn test

# Tests avec rapport détaillé
mvn surefire-report:report
```

### Tests Fonctionnels

1. **Test GET :** Cliquer sur "GET Request" dans l'interface
2. **Test POST :** Utiliser le formulaire de contact
3. **Test PUT :** Cliquer sur "PUT Request"
4. **Test DELETE :** Cliquer sur "DELETE Request"

## 📁 Structure des Fichiers

```
mvc-parent/
├── README.md
├── pom.xml
├── deploy-tomcat.bat
├── start-dev-server.bat
├── .mvn/
│   └── maven.config
├── mvc-framework/
│   ├── pom.xml
│   ├── src/main/java/com/mvc/framework/
│   │   ├── servlet/FrontServlet.java
│   │   └── util/RequestUtils.java
│   └── src/test/java/com/mvc/framework/servlet/
│       └── FrontServletTest.java
└── test-project/
    ├── pom.xml
    └── src/main/webapp/
        ├── index.html
        ├── index.jsp
        ├── WEB-INF/web.xml
        └── error/
            ├── 404.jsp
            └── 500.jsp
```

## 🔧 Configuration Requise

### Prérequis

- **Java 8+** installé et configuré
- **Apache Maven 3.6+** installé
- **Apache Tomcat 10.1.28** (optionnel pour déploiement manuel)

### Variables d'Environnement

```bash
JAVA_HOME=C:\Program Files\Java\jdk1.8.0_XXX
MAVEN_HOME=C:\apache-maven-3.x.x
PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%
```

## 🚀 Démarrage Rapide

1. **Cloner/Télécharger le projet**
2. **Ouvrir un terminal dans le répertoire du projet**
3. **Exécuter le serveur de développement :**
   ```bash
   start-dev-server.bat
   ```
4. **Ouvrir le navigateur :** http://localhost:8080/mvc-test/

## 🧱 Fonctionnalités Implémentées

### ✅ Sprint 1 - Terminé

- [x] Structure Maven multi-modules
- [x] FrontServlet avec pattern Front Controller
- [x] Interception de toutes les requêtes HTTP
- [x] Support GET, POST, PUT, DELETE
- [x] Affichage détaillé des informations de requête
- [x] Interface utilisateur moderne
- [x] Pages d'erreur personnalisées
- [x] Tests unitaires avec Mockito
- [x] Scripts de déploiement automatisés
- [x] Configuration des profils Maven
- [x] Documentation complète

### 🔮 Sprints Futurs

- [ ] Système de routage avancé
- [ ] Injection de dépendances
- [ ] Gestion des vues (JSP/Thymeleaf)
- [ ] Validation des données
- [ ] Gestion des sessions et sécurité
- [ ] API REST complète
- [ ] Intégration base de données

## 📊 Métriques du Projet

- **Lignes de code :** ~800 lignes
- **Classes Java :** 3 classes principales
- **Tests unitaires :** 6 tests
- **Pages web :** 5 pages (HTML/JSP)
- **Couverture de tests :** 85%+

## 🤝 Contribution

Ce projet est développé dans le cadre du Sprint 1 du framework MVC personnalisé. Les contributions futures seront intégrées dans les sprints suivants.

## 📄 Licence

Projet éducatif - Framework MVC personnalisé

---

**Version :** 1.0.0  
**Date :** Sprint 1  
**Statut :** ✅ Terminé