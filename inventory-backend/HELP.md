
Documentation de démarrage de l'application
Prérequis

Avant de démarrer l'application, assurez-vous d'avoir installé les éléments suivants sur votre machine :

   Java 21 : Assurez-vous que JDK 21 est installé et configuré sur votre système.

   Docker : Veillez à ce que Docker soit installé et en état de marche.

   Docker Compose doit également être disponible.

   Maven : Pour exécuter et construire le projet.

   pgAdmin (optionnel) : Pour visualiser la base PostgreSQL si nécessaire.

Étapes pour démarrer l'application
1. Lancer la base de données PostgreSQL avec Docker Compose

L'application utilise une base de données PostgreSQL, qui doit être démarrée en premier.

Naviguez vers le répertoire contenant le fichier docker-compose.yml à la racine du projet.

Démarre la base PostgreSQL avec la commande suivante :

docker-compose up -d


Cette commande initialise et démarre le conteneur PostgreSQL en arrière-plan.

Vérifiez que le conteneur est actif :

docker-compose ps

Vous devriez voir inventory-postgres avec le statut Up.

(Optionnel) Configurer pgAdmin pour se connecter à la base :

Host name/address : localhost

Port : 5432

Maintenance database : inventory_db

Username : admin

Password : admin

2. Configurer application.properties

    Dans src/main/resources/application.properties, assurez-vous que la configuration correspond à la base Docker :

spring.application.name=inventory-backend

spring.datasource.url=jdbc:postgresql://localhost:5432/inventory_db
spring.datasource.username=admin
spring.datasource.password=admin


Important : les infos doivent correspondre à celles du docker-compose.yml.

3. Démarrer l'application Spring Boot

Assurez-vous que la base PostgreSQL est bien en cours d'exécution.

Depuis le répertoire du projet, exécutez :

./mvnw clean install
./mvnw spring-boot:run


L'application sera disponible à l'URL suivante :

http://localhost:8080

4. Swagger UI

Une fois l'application démarrée, la documentation API générée par Swagger est accessible ici :

http://localhost:8080/api/swagger-ui/index.html

5. Vérification

Base de données : Connectez-vous via pgAdmin ou psql pour vérifier que PostgreSQL fonctionne et que les tables sont créées (Flyway gère les migrations).

Spring Boot : Vérifiez la console pour confirmer que l'application démarre sans erreurs et que Flyway initialise correctement la base.

Alors En résumé

Installer les prérequis : Java 21, Docker, Maven.

Lancer PostgreSQL avec Docker Compose.

Vérifier la configuration dans application.properties.

Démarrer Spring Boot avec Maven.

Accéder à l'application sur http://localhost:8080 et Swagger sur /api/swagger-ui/index.html.

L'application est maintenant prête à être utilisée !