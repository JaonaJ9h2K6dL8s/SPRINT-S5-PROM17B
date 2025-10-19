# Framework Sprint 2-bis - Auto-découverte et Routage Avancé

## 🚀 Nouvelles Fonctionnalités

Le Sprint 2-bis apporte des améliorations significatives au framework avec l'introduction de l'auto-découverte des contrôleurs et un routage plus intelligent.

### ✨ Fonctionnalités Principales

1. **Auto-découverte des Contrôleurs** 
   - Nouvelle annotation `@Controller` pour marquer les classes contrôleurs
   - Scan automatique des packages pour détecter les contrôleurs
   - Support multi-packages (controller, controllerBe, controllerKely)

2. **Routage Intelligent Amélioré**
   - Amélioration du `RooterServlet` avec scan automatique
   - Support des valeurs de retour des méthodes
   - Interface utilisateur moderne avec affichage des résultats

3. **Compatibilité Ascendante**
   - Support des contrôleurs sans annotation `@Controller`
   - Maintien de la compatibilité avec le Sprint 2

## 📁 Structure du Projet

```
sprint-2-bis/
├── src/
│   └── main/
│       ├── java/
│       │   ├── jframework/
│       │   │   ├── annotation/
│       │   │   │   ├── Controller.java    # Nouvelle annotation
│       │   │   │   └── Url.java          # Annotation existante
│       │   │   ├── servlet/
│       │   │   │   └── RooterServlet.java # Servlet amélioré
│       │   │   └── util/
│       │   │       └── ControllerScanner.java # Utilitaire de scan
│       │   └── project/
│       │       ├── controller/
│       │       │   └── Personne.java     # Contrôleur principal
│       │       ├── controllerBe/
│       │       │   └── Voiture.java      # Nouveau contrôleur
│       │       └── controllerKely/
│       │           └── Materiaux.java    # Nouveau contrôleur
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml              # Configuration améliorée
│           └── annotation.jsp           # Page de démonstration
├── lib/                                 # Bibliothèques
├── build.bat                           # Script de build amélioré
└── README.md                           # Cette documentation
```

## 🎯 Annotations Disponibles

### @Controller
```java
@Controller
public class MonControleur {
    // Classe automatiquement découverte
}
```

### @Url
```java
@Url("/mon-url")
public String maMethode() {
    return "Résultat affiché dans l'interface";
}
```

## 🔧 Utilisation

### 1. Compilation
```bash
cd sprint-2-bis
build.bat
```

### 2. Déploiement
Copiez le fichier `framework-sprint2bis-test.war` dans le répertoire `webapps` de Tomcat.

### 3. Test
Accédez aux URLs suivantes après déploiement :

#### Contrôleur Personne
- `http://localhost:8080/framework-sprint2bis-test/oui`
- `http://localhost:8080/framework-sprint2bis-test/personne`
- `http://localhost:8080/framework-sprint2bis-test/info`
- `http://localhost:8080/framework-sprint2bis-test/details`

#### Contrôleur Voiture
- `http://localhost:8080/framework-sprint2bis-test/voiture`
- `http://localhost:8080/framework-sprint2bis-test/voiture/details`
- `http://localhost:8080/framework-sprint2bis-test/voiture/specs`

#### Contrôleur Materiaux
- `http://localhost:8080/framework-sprint2bis-test/materiaux`
- `http://localhost:8080/framework-sprint2bis-test/materiaux/fournisseur`
- `http://localhost:8080/framework-sprint2bis-test/materiaux/count`

#### Page de Démonstration
- `http://localhost:8080/framework-sprint2bis-test/annotation.jsp`

## 🆕 Améliorations par rapport au Sprint 2

| Aspect | Sprint 2 | Sprint 2-bis |
|--------|----------|--------------|
| **Découverte des contrôleurs** | Manuelle | Automatique avec @Controller |
| **Scan des packages** | Package unique | Multi-packages |
| **Interface utilisateur** | Basique | Moderne avec CSS avancé |
| **Retour des méthodes** | Non affiché | Affiché dans l'interface |
| **Utilitaires** | Aucun | ControllerScanner |
| **Documentation** | Limitée | Complète avec exemples |

## 🛠️ Technologies Utilisées

- **Java** : Langage principal
- **Jakarta EE** : Servlets et API web
- **Réflexion Java** : Auto-découverte et invocation dynamique
- **HTML/CSS/JSP** : Interface utilisateur moderne
- **Annotations** : @Controller et @Url

## 📝 Exemples de Code

### Création d'un Nouveau Contrôleur

```java
package project.controller;

import jframework.annotation.Controller;
import jframework.annotation.Url;

@Controller
public class MonNouveauControleur {
    
    @Url("/nouveau")
    public String maNouvelleFonctionnalite() {
        return "Nouvelle fonctionnalité implémentée !";
    }
}
```

### Scan Programmatique des Contrôleurs

```java
import jframework.util.ControllerScanner;

// Scanner un package pour les contrôleurs
List<Class<?>> controllers = ControllerScanner.findControllerClasses("project.controller");

// Afficher les contrôleurs trouvés
ControllerScanner.printControllerClasses("project.controller");
```

## 🔍 Débogage

### Logs de Démarrage
Le servlet affiche dans la console les contrôleurs et méthodes découverts :
```
Instance de Personne créée avec @Controller
Instance de Voiture créée avec @Controller
Instance de Materiaux créée avec @Controller
```

### Page de Diagnostic
La page `annotation.jsp` affiche :
- Tous les contrôleurs découverts
- Leurs méthodes annotées avec @Url
- Statistiques du framework
- Liens de test directs

## 🚀 Évolutions Futures

- Support des paramètres de méthode
- Injection de dépendances
- Gestion des sessions
- Support des templates
- API REST automatique

## 📄 Licence

Ce framework est développé dans le cadre d'un projet éducatif de développement par sprints itératifs.

---

**Framework Sprint 2-bis** - Auto-découverte et Routage Avancé  
*Développement par sprints itératifs*