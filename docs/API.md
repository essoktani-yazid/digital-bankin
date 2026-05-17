# Référence API — Digital Banking

> Documentation technique exhaustive des endpoints REST exposés par le backend Spring Boot.

- **URL de base (Développement) :** `http://localhost:8085`
- **Format d'échange :** `application/json`
- **Documentation interactive (Swagger UI) :** `http://localhost:8085/swagger-ui.html`
- **Spécification OpenAPI (v3) :** `http://localhost:8085/v3/api-docs`

---

## Authentification & Autorisation

L'API utilise une authentification stateless sécurisée par jetons **JWT (JSON Web Token)** signés via l'algorithme HMAC-SHA512 (`MacAlgorithm.HS512`).

À l'exception de l'endpoint de connexion (`/auth/login`), toutes les requêtes HTTP doivent contenir un jeton JWT valide dans l'en-tête HTTP :

```http
Authorization: Bearer <votre_token_jwt>
```

### Comptes de test préconfigurés

| Identifiant | Mot de passe | Rôles / Autorités | Droits accordés |
| :--- | :--- | :--- | :--- |
| `user1` | `1234` | `SCOPE_USER` | Consultation des clients, comptes et opérations. |
| `user2` | `1235` | `SCOPE_USER`, `SCOPE_ADMIN` | Consultation et opérations d'administration (création, modification, suppression). |

---

## 1. Module Authentification (`/auth`)

### 1.1 Obtenir un jeton d'accès (Login)

Permet d'authentifier un utilisateur et de recevoir un jeton JWT d'une durée de validité de 10 minutes.

- **URL :** `/auth/login`
- **Méthode :** `POST`
- **Authentification requise :** Non
- **Paramètres (Query Parameters ou Form Data) :**
  - `username` *(requis)* : Nom d'utilisateur (ex: `user2`)
  - `password` *(requis)* : Mot de passe (ex: `1235`)

#### Réponse de succès (`200 OK`)

```json
{
  "access-token": "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoidXNlcjIiLCJzY29wZSI6IlVTRVIgQURNSU4iLCJleHAiOjE3Mjk4NzI2MDB9..."
}
```

---

### 1.2 Profil utilisateur courant

Retourne les informations d'authentification et les autorités associées au jeton JWT envoyé.

- **URL :** `/auth/profile`
- **Méthode :** `GET`
- **Authentification requise :** Oui (`Bearer Token`)

#### Réponse de succès (`200 OK`)

```json
{
  "name": "user2",
  "authorities": [
    { "authority": "SCOPE_USER" },
    { "authority": "SCOPE_ADMIN" }
  ],
  "authenticated": true
}
```

---

## 2. Module Gestion des Clients (`/customers`)

### 2.1 Lister tous les clients

Récupère l'ensemble des clients enregistrés dans la banque.

- **URL :** `/customers`
- **Méthode :** `GET`
- **Authentification requise :** Oui (`SCOPE_USER`)

#### Réponse de succès (`200 OK`)

```json
[
  {
    "id": 1,
    "name": "Hassan",
    "email": "hassan@gmail.com"
  },
  {
    "id": 2,
    "name": "Imane",
    "email": "imane@gmail.com"
  }
]
```

---

### 2.2 Rechercher des clients par mot-clé

Recherche des clients dont le nom contient la chaîne fournie en paramètre.

- **URL :** `/customers/search`
- **Méthode :** `GET`
- **Authentification requise :** Oui (`SCOPE_USER`)
- **Paramètres de requête (Query Params) :**
  - `keyword` *(optionnel, défaut: "")* : Sous-chaîne recherchée dans le nom

#### Réponse de succès (`200 OK`)

```json
[
  {
    "id": 1,
    "name": "Hassan",
    "email": "hassan@gmail.com"
  }
]
```

---

### 2.3 Obtenir les détails d'un client

Récupère les informations d'un client à partir de son identifiant numérique.

- **URL :** `/customers/{id}`
- **Méthode :** `GET`
- **Authentification requise :** Oui (`SCOPE_USER`)

#### Réponse de succès (`200 OK`)

```json
{
  "id": 1,
  "name": "Hassan",
  "email": "hassan@gmail.com"
}
```

#### Réponse en cas d'erreur (`404 Not Found`)

```json
{
  "message": "Customer not found"
}
```

---

### 2.4 Créer un nouveau client

Enregistre un nouveau client dans la base de données.

- **URL :** `/customers`
- **Méthode :** `POST`
- **Authentification requise :** Oui (`SCOPE_ADMIN`)

#### Corps de la requête (Request Body)

```json
{
  "name": "Yazid ESSOKTANI",
  "email": "yazid@gmail.com"
}
```

#### Réponse de succès (`200 OK`)

```json
{
  "id": 4,
  "name": "Yazid ESSOKTANI",
  "email": "yazid@gmail.com"
}
```

---

### 2.5 Modifier un client existant

Met à jour les informations d'un client spécifié par son identifiant.

- **URL :** `/customers/{customerId}`
- **Méthode :** `PUT`
- **Authentification requise :** Oui (`SCOPE_ADMIN`)

#### Corps de la requête (Request Body)

```json
{
  "name": "Yazid ESSOKTANI",
  "email": "contact@essoktani.tech"
}
```

#### Réponse de succès (`200 OK`)

```json
{
  "id": 4,
  "name": "Yazid ESSOKTANI",
  "email": "contact@essoktani.tech"
}
```

---

### 2.6 Supprimer un client

Supprime un client ainsi que ses comptes bancaires associés en cascade.

- **URL :** `/customers/{id}`
- **Méthode :** `DELETE`
- **Authentification requise :** Oui (`SCOPE_ADMIN`)

#### Réponse de succès (`200 OK`)

*Corps de réponse vide.*

---

## 3. Module Gestion des Comptes & Opérations (`/accounts`)

### 3.1 Lister tous les comptes bancaires

Retourne la liste polymorphique des comptes bancaires (comptes courants et comptes d'épargne).

- **URL :** `/accounts`
- **Méthode :** `GET`
- **Authentification requise :** Oui

#### Réponse de succès (`200 OK`)

```json
[
  {
    "type": "CurrentAccount",
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "balance": 74500.0,
    "createdAt": "2026-10-25T12:00:00.000+00:00",
    "status": "CREATED",
    "customerDTO": {
      "id": 1,
      "name": "Hassan",
      "email": "hassan@gmail.com"
    },
    "overDraft": 9000.0
  },
  {
    "type": "SavingAccount",
    "id": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
    "balance": 112000.0,
    "createdAt": "2026-10-25T12:00:00.000+00:00",
    "status": "CREATED",
    "customerDTO": {
      "id": 1,
      "name": "Hassan",
      "email": "hassan@gmail.com"
    },
    "interestRate": 5.5
  }
]
```

---

### 3.2 Obtenir un compte bancaire par son identifiant

Consulte le détail d'un compte bancaire à partir de son UUID.

- **URL :** `/accounts/{accountId}`
- **Méthode :** `GET`
- **Authentification requise :** Oui

#### Réponse de succès (`200 OK`)

```json
{
  "type": "CurrentAccount",
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "balance": 74500.0,
  "createdAt": "2026-10-25T12:00:00.000+00:00",
  "status": "CREATED",
  "customerDTO": {
    "id": 1,
    "name": "Hassan",
    "email": "hassan@gmail.com"
  },
  "overDraft": 9000.0
}
```

---

### 3.3 Historique paginé des opérations d'un compte

Récupère l'historique paginé des transactions (débits et crédits) associées à un compte bancaire donné.

- **URL :** `/accounts/{accountId}/pageOperations`
- **Méthode :** `GET`
- **Authentification requise :** Oui
- **Paramètres de requête (Query Params) :**
  - `page` *(optionnel, défaut: 0)* : Index de la page (commence à 0)
  - `size` *(optionnel, défaut: 5)* : Nombre d'opérations par page

#### Réponse de succès (`200 OK`)

```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "balance": 74500.0,
  "currentPage": 0,
  "totalPages": 4,
  "pageSize": 5,
  "accountOperationDTOS": [
    {
      "id": 20,
      "operationDate": "2026-10-25T14:32:00.000+00:00",
      "amount": 2500.0,
      "type": "DEBIT",
      "description": "Debit"
    },
    {
      "id": 19,
      "operationDate": "2026-10-25T14:30:00.000+00:00",
      "amount": 15000.0,
      "type": "CREDIT",
      "description": "Credit"
    }
  ]
}
```

---

### 3.4 Liste brute de toutes les opérations d'un compte

Retourne l'intégralité des opérations d'un compte sans pagination.

- **URL :** `/accounts/{accountId}/operations`
- **Méthode :** `GET`
- **Authentification requise :** Oui

#### Réponse de succès (`200 OK`)

```json
[
  {
    "id": 20,
    "operationDate": "2026-10-25T14:32:00.000+00:00",
    "amount": 2500.0,
    "type": "DEBIT",
    "description": "Debit"
  }
]
```

---

### 3.5 Effectuer un débit

Débite un montant spécifié du compte bancaire après vérification de provision.

- **URL :** `/accounts/debit`
- **Méthode :** `POST`
- **Authentification requise :** Oui

#### Corps de la requête (Request Body)

```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 1200.0,
  "description": "Retrait DAB"
}
```

#### Réponse de succès (`200 OK`)

```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 1200.0,
  "description": "Retrait DAB"
}
```

---

### 3.6 Effectuer un crédit

Crédite un montant spécifié sur le compte bancaire et enregistre l'opération.

- **URL :** `/accounts/credit`
- **Méthode :** `POST`
- **Authentification requise :** Oui

#### Corps de la requête (Request Body)

```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 3500.0,
  "description": "Dépôt d'espèces"
}
```

#### Réponse de succès (`200 OK`)

```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 3500.0,
  "description": "Dépôt d'espèces"
}
```

---

### 3.7 Effectuer un virement inter-comptes

Exécute un virement atomique en débitant le compte source et en créditant le compte destination.

- **URL :** `/accounts/transfer`
- **Méthode :** `POST`
- **Authentification requise :** Oui

#### Corps de la requête (Request Body)

```json
{
  "accountSource": "550e8400-e29b-41d4-a716-446655440000",
  "accountDestination": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
  "amount": 500.0,
  "description": "Virement mensuel"
}
```

#### Réponse de succès (`200 OK`)

*Corps de réponse vide.*

---

## 4. Codes de statut et d'erreurs

| Code HTTP | Signification | Description |
| :--- | :--- | :--- |
| `200 OK` | Succès | Requête exécutée avec succès (consultation, mise à jour, transaction). |
| `400 Bad Request` | Requête invalide | Paramètres obligatoires manquants ou corps JSON mal formé. |
| `401 Unauthorized` | Non authentifié | Jeton JWT manquant, invalide ou expiré. |
| `403 Forbidden` | Accès refusé | Droits insuffisants (ex: action requérant `SCOPE_ADMIN` tentée avec `SCOPE_USER`). |
| `404 Not Found` | Ressource introuvable | Le client (`CustomerNotFoundException`) ou le compte (`BankAccountNotFoundException`) n'existe pas. |
| `500 Internal Error` | Erreur interne / Solde insuffisant | Erreur serveur ou exception métier (`BalanceNotSufficientException`). |

---
[⬅ Retour au README principal](../README.md)
