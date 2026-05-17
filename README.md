<div align="center">
  <!-- En-tête créatif -->
  <img src="https://capsule-render.vercel.app/api?type=waving&color=00B4D8&height=150&section=header&text=Digital%20Banking&fontSize=50&fontAlignY=35&fontColor=ffffff" width="100%"/>

  <!-- Animation texte ciblant "Ce que fait le projet" et "Pourquoi c'est utile" -->
  <img src="https://readme-typing-svg.herokuapp.com?font=JetBrains+Mono&weight=700&size=20&duration=3000&pause=1000&color=00B4D8&center=true&vCenter=true&width=600&lines=Gestion+de+Comptes+Bancaires+et+Clients;Operations+de+Credit%2C+Debit+%26+Virement;Securite+Spring+Security+JWT+OAuth2;Architecture+Full--Stack+Spring+Boot+%26+Angular" alt="Typing SVG" />

  <br/>

  <!-- Badges recommandés par GitHub -->
  <p>
    <img src="https://img.shields.io/badge/version-1.0.0-blue?style=for-the-badge" alt="Version">
    <img src="https://img.shields.io/github/license/essoktani-yazid/digital-bankin?style=for-the-badge" alt="License">
  </p>
</div>

---

<!-- 1. CE QUE FAIT LE PROJET -->
## À propos du projet

**Digital Banking** est une solution full-stack moderne conçue pour la gestion dématérialisée et sécurisée des clients, des comptes bancaires (comptes courants et comptes d'épargne) ainsi que du traitement transactionnel des flux financiers. Elle permet aux gestionnaires et aux utilisateurs de consulter les comptes, d'effectuer des opérations de crédit, de débit et des virements inter-comptes en temps réel sans avoir à manipuler des procédures manuelles ou des systèmes disparates.

> [!NOTE]
> Pour une vue détaillée des choix de conception et des schémas système, consultez la [documentation d'architecture](docs/ARCHITECTURE.md).

<!-- 2. POURQUOI LE PROJET EST UTILE -->
## Pourquoi utiliser ce projet ?

Ce projet répond à la problématique de modernisation et de sécurisation des opérations bancaires quotidiennes en apportant des avantages concrets :

- **Gestion polymorphique des comptes bancaires :** Prise en charge native des comptes courants (avec découvert autorisé) et des comptes d'épargne (avec taux d'intérêt), modélisés via une hiérarchie JPA en table unique (`SINGLE_TABLE`).
- **Opérations transactionnelles en temps réel :** Traitement unitaire et atomique des débits (avec contrôle strict du solde), crédits et virements inter-comptes (`@Transactional`), enregistrant un journal d'opérations détaillé et paginé.
- **Sécurité et contrôle d'accès strict (RBAC) :** Authentification stateless par jeton JWT signé avec HMAC-SHA512 et gestion fine des autorisations par rôles (`SCOPE_USER` pour la consultation, `SCOPE_ADMIN` pour la gestion des clients).
- **Interface réactive et intuitive :** Application Angular 21 exploitant la programmation réactive (RxJS) et Bootstrap 5 pour une navigation fluide et des recherches asynchrones instantanées.

### Stack Technique

| Couche | Technologies |
| :--- | :--- |
| **Backend & API** | Java 21, Spring Boot 4.0, Spring Data JPA, Spring Security (OAuth2 Resource Server / JWT) |
| **Frontend / UI** | Angular 21, TypeScript, RxJS, Bootstrap 5, Bootstrap Icons |
| **Données & Persistance** | MySQL (production/développement), H2 Database (tests & console H2), Hibernate ORM |
| **Documentation & Outils** | SpringDoc OpenAPI (Swagger UI v2.6.0), Lombok, Apache Maven |

<!-- 3. COMMENT DÉMARRER -->
## Pour commencer

Suivez ces instructions pour déployer et exécuter une instance locale du projet pour le développement et les tests.

### Prérequis

Assurez-vous d'avoir installé les outils suivants sur votre environnement :

- `Java Development Kit (JDK)` (v21+)
- `Apache Maven` (v3.9+) *(ou utiliser le script `./mvnw` intégré)*
- `Node.js` (v20+ ou v22+) & `npm` (v10+)
- `MySQL Server` (v8.0+) *(ou utiliser la base de données en mémoire H2)*

### Installation

1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/essoktani-yazid/digital-bankin.git
   cd digital-bankin
   ```

2. **Configurer la base de données**
   Vérifiez le fichier `backend/src/main/resources/application.properties` :
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/e-bank?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=P@ssw0rd
   server.port=8085
   ```
   > [!IMPORTANT]
   > Assurez-vous que votre serveur MySQL est démarré sur le port 3306. Si vous préférez exécuter l'application sans base MySQL externe, vous pouvez activer la ligne `spring.datasource.url=jdbc:h2:mem:bank` pour exploiter la base en mémoire H2.

3. **Démarrer le backend (Spring Boot)**
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
   *L'API REST sera disponible sur : `http://localhost:8085`*

4. **Démarrer le frontend (Angular)**
   Dans un second terminal :
   ```bash
   cd frontend
   npm install
   npm start
   ```
   *L'interface web sera accessible sur : `http://localhost:4200`*

### Exemple d'utilisation

Une fois les services démarrés, l'API backend est accessible sur `http://localhost:8085` et l'interface utilisateur sur `http://localhost:4200`.

Exemple d'appel pour authentifier un utilisateur et récupérer son jeton JWT :

```bash
# 1. Connexion (obtention du token JWT)
curl -X POST "http://localhost:8085/auth/login?username=user2&password=1235"
```

Exemple de réponse :
```json
{
  "access-token": "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoidXNlcjIiLCJzY29wZSI6IlVTRVIgQURNSU4iLCJleHAiOjE3Mjk4NzI2MDB9..."
}
```

Exemple d'opération de débit sur un compte bancaire :

```bash
# 2. Exécution d'un débit bancaire
curl -X POST http://localhost:8085/accounts/debit \
  -H "Authorization: Bearer <access-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "550e8400-e29b-41d4-a716-446655440000",
    "amount": 500.0,
    "description": "Retrait distributeur"
  }'
```

*Pour découvrir l'ensemble des endpoints et paramètres disponibles, consultez la [Référence API complète](docs/API.md) ou l'interface Swagger sur `http://localhost:8085/swagger-ui.html`.*

### Structure du projet

```text
.
├── backend/                             # Application Backend Spring Boot
│   ├── src/main/java/tech/essoktani/backend/
│   │   ├── dtos/                        # Objets de transfert de données (DTOs)
│   │   ├── entities/                    # Entités JPA (Customer, BankAccount, AccountOperation...)
│   │   ├── enums/                       # Énumérations métier (AccountStatus, OperationType)
│   │   ├── exceptions/                  # Exceptions personnalisées (BalanceNotSufficient...)
│   │   ├── mappers/                     # Mappage entre entités et DTOs
│   │   ├── repositories/                # Couche d'accès aux données Spring Data JPA
│   │   ├── security/                    # Configuration Spring Security & JWT
│   │   ├── services/                    # Logique métier et gestion transactionnelle
│   │   └── web/                         # Contrôleurs REST (CustomerRestController, BankAccountRestAPI)
│   ├── src/main/resources/              # Configuration (application.properties)
│   └── pom.xml                          # Dépendances Maven
├── frontend/                            # Application Frontend Angular 21
│   ├── src/app/
│   │   ├── components/                  # Composants (account, customer, navbar, new-customer)
│   │   ├── environments/                # Configuration des environnements (backendHost: 8085)
│   │   ├── models/                      # Interfaces TypeScript (Customer...)
│   │   ├── services/                    # Services Angular (CustomerService...)
│   │   ├── app.routes.ts                # Routage de l'application
│   │   └── app.config.ts                # Configuration Angular standalone
│   └── package.json                     # Dépendances et scripts npm
├── docs/                                # Documentation technique
│   ├── assets/                          # Captures d'écran et schémas
│   ├── API.md                           # Documentation complète des endpoints REST
│   └── ARCHITECTURE.md                  # Conception architecturale et flux Mermaid
├── LICENCE                              # Licence MIT
└── README.md                            # Documentation principale
```

<!-- 4. AIDE & SUPPORT -->
## Aide et Support

Vous avez une question sur l'architecture ou l'implémentation du projet ?
- **Documentation :** Consultez les fichiers techniques présents dans le dossier [`docs/`](docs/).
- **Contact direct :** N'hésitez pas à me contacter directement via LinkedIn ou mon portfolio.

<!-- 5. CONTRIBUTION & AUTEUR -->
## Contribuer

Les contributions et propositions d'améliorations sont les bienvenues !
1. **Forkez** le projet
2. Créez votre branche de fonctionnalité (`git checkout -b feature/NouvelleFonctionnalite`)
3. Commitez vos changements (`git commit -m 'feat: Ajout de NouvelleFonctionnalite'`)
4. Pushez vers la branche (`git push origin feature/NouvelleFonctionnalite`)
5. Ouvrez une **Pull Request**

## Auteur

<div align="center">

**Yazid ESSOKTANI**  
*Full-Stack Web & AI Developer*

[![Portfolio](https://img.shields.io/badge/Portfolio-essoktani.tech-00B4D8?style=for-the-badge&logo=google-chrome&logoColor=white)](https://www.essoktani.tech/)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Yazid_ESSOKTANI-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/yazid-essoktani-337549251)
[![GitHub](https://img.shields.io/badge/GitHub-@essoktani--yazid-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/essoktani-yazid)

</div>

---

## Licence

Distribué sous la licence **MIT**. Consultez le fichier [`LICENCE`](LICENCE) pour plus d'informations.
