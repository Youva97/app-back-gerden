# Backend de l'application Gerden

## <span style="color: #21618C; text-decoration: underline;"> Prérequis </span>

Avant de lancer l'application, assurez-vous d'avoir les éléments suivants installés sur votre machine :

1. **Java Development Kit (JDK) 11** ou supérieur.
2. **Apache Maven** pour la gestion des dépendances et la construction du projet.
3. **MySQL** (ou tout autre SGBD compatible) pour la base de données.

## <span style="color: #21618C; text-decoration: underline;"> Configuration de la Base de Données </span>

1. **Créer une base de données MySQL :**
   ```sql
   CREATE DATABASE leadb;

2. **Configurer les paramètres de la base de données dans application.properties :**
    ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/leadb
   spring.datasource.username=nom_utilisateur
   spring.datasource.password=mot_de_passe
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
    
## <span style="color: #21618C; text-decoration: underline;"> Installation et Lancement de l'Application </span>

1. **Cloner le projet :**
   ```shell
   git clone "Mettre le lien de mon git" 
   cd association-lea-app-back

2. **Construire le projet avec Maven :**
   ```shell
   mvn clean install

3. **Lancer l'application :**
   ```shell
   mvn spring-boot:run
   
   Ou bien, vous pouvez également exécuter le fichier jar généré :
   
   java -jar target/votre-application.jar
   
## <span style="color: #21618C; text-decoration: underline;"> Accéder à l'Application </span>
   
       http://localhost:8082

## <span style="color: #21618C; text-decoration: underline;"> Utilisation de Postman pour Tester l'API </span>

### Ajouter un Utilisateur

1. URL :
   POST http://localhost:8082/utilisateurs/ajouter
2. Headers :
   Content-Type: application/json
3. Body :
   ```json
      {
      "nom": "Dupont",
      "prenom": "Jean",
      "email": "jean.dupont@example.com",
      "motDePasse": "motdepasse123",
      "type": "parent",
      "fonction": "Ingenieur",
      "adresse": {
         "numeroRue": "123",
         "nomRue": "Rue de la Paix",
         "complementAdresse": "",
         "codePostal": "75001",
         "ville": "Paris",
         "pays": "France"
         },
      "telephone": "0123456789"
      }

### Autres Endpoints

- Lister tous les utilisateurs : GET http://localhost:8082/utilisateurs/tous
- Modifier un utilisateur : PUT http://localhost:8082/utilisateurs/modifier
- Supprimer un utilisateur : DELETE http://localhost:8082/utilisateurs/{id}
- Ajouter un enfant à un utilisateur : POST http://localhost:8082/utilisateurs/{userId}/enfants/ajouter
- Modifier un enfant : PUT http://localhost:8082/utilisateurs/{userId}/enfants/modifier
- Lister les enfants d'un utilisateur : GET http://localhost:8082/utilisateurs/{userId}/enfants
- Supprimer un enfant : DELETE http://localhost:8082/utilisateurs/{userId}/enfants/{enfantId}


## <span style="color: #21618C; text-decoration: underline;"> Déployer l'application sur un conteneur docker </span>
Le projet Association-lea-app-back est déployable sur des conteneurs docker grâce au fichier **docker-compose.yml**
### Le ficher va faire les actions suivantes :
- Builder l’application Association-lea-app-back
- Création de l’image et le conteneur de l’application Association-lea-app-back,
- Création de l’image et le conteneur de la base de données Postgres  leadb
### Pour exécuter ce fichier en local, il faut suivre les étapes suivantes :
- Installer docker sur le poste local (docker desktop)
- Installer postgresDb en local
- Adapté les infos de connexion à la base en local (Id/Password) avec les infos de connexion qui sont renseigné sur le fichier. env

Pour tester le bon fonctionnement de l’application sur les conteneurs, vous pouvez faire des requêtes Postman sur le port 8000 qui est renseigné sur le fichier compose.yml et qui peut être changé aussi si besoin
Voici un exemple de requête GET sur tous les guides qui sont sur la base http://localhost:8000/guides/tous


## <span style="color: #21618C; text-decoration: underline;"> Contribution </span>

1. Forkez ce dépôt.
2. Créez une branche pour votre fonctionnalité (**git checkout -b Feature/ma-fonctionnalite**).
3. Commitez vos modifications (**git commit -am 'Ajout de ma fonctionnalité'**).
4. Poussez votre branche (**git push origin Feature/ma-fonctionnalite**).
5. Ouvrez une Pull Request.

## <span style="color: #21618C; text-decoration: underline;"> Licence </span>

Ce projet est sous licence MIT - voir les [cgu](https://www.asso-lea.org/mentions-legales.html) pour plus de détails.


```rust
 Ce `README.md` guide l'utilisateur à travers le processus de configuration et de lancement
 de l'application Spring Boot en local, y compris la configuration de la base de données, 
 la construction et le lancement de l'application, ainsi que l'utilisation de Postman pour 
 tester l'API.

