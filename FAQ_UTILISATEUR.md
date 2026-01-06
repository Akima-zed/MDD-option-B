# FAQ Utilisateur - MDD (Monde de Dév)

## 📚 Table des matières
- [Démarrage](#démarrage)
- [Inscription et connexion](#inscription-et-connexion)
- [Navigation](#navigation)
- [Thèmes et abonnements](#thèmes-et-abonnements)
- [Articles](#articles)
- [Commentaires](#commentaires)
- [Profil](#profil)
- [Erreurs courantes](#erreurs-courantes)
- [Sécurité](#sécurité)

---

## Démarrage

### Comment accéder à l'application ?

1. Ouvrez votre navigateur web (Chrome, Firefox, Safari, Edge)
2. Accédez à l'URL : `http://localhost:4200`
3. Vous arrivez sur la page d'accueil avec deux options :
   - **S'inscrire** (si vous n'avez pas de compte)
   - **Se connecter** (si vous avez déjà un compte)

### Quels navigateurs sont supportés ?

- ✅ Google Chrome (recommandé)
- ✅ Mozilla Firefox
- ✅ Microsoft Edge
- ✅ Safari (macOS/iOS)
- ⚠️ Internet Explorer : Non supporté

---

## Inscription et connexion

### Comment créer un compte ?

1. Sur la page d'accueil, cliquez sur **"S'INSCRIRE"**
2. Remplissez le formulaire :
   - **Nom d'utilisateur** : 3 caractères minimum (ex: `devjohn`)
   - **Email** : Format valide requis (ex: `john@example.com`)
   - **Mot de passe** : 8 caractères minimum avec :
     - Au moins 1 chiffre
     - Au moins 1 lettre minuscule
     - Au moins 1 lettre majuscule
     - Au moins 1 caractère spécial (!, @, #, $, etc.)
3. Cliquez sur **"S'INSCRIRE"**
4. Vous êtes automatiquement connecté et redirigé vers votre fil d'actualité

**Exemple de mot de passe valide** : `MotDePasse123!`

### Comment me connecter ?

1. Sur la page d'accueil, cliquez sur **"SE CONNECTER"**
2. Saisissez :
   - **Email OU nom d'utilisateur** (les deux fonctionnent)
   - **Mot de passe**
3. Cliquez sur **"SE CONNECTER"**
4. Vous êtes redirigé vers votre fil d'actualité

**Astuce** : Vous pouvez utiliser soit votre email, soit votre nom d'utilisateur pour vous connecter.

### Comment me déconnecter ?

1. Cliquez sur l'icône **menu** (☰) en haut à gauche
2. Cliquez sur **"Me déconnecter"**
3. Vous êtes redirigé vers la page d'accueil

---

## Navigation

### Comment naviguer dans l'application ?

Le menu principal (icône ☰ en haut à gauche) donne accès à :

| Section | Description |
|---------|-------------|
| **ARTICLES** | Retour au fil d'actualité |
| **THÈMES** | Liste des thèmes disponibles |
| **ME** | Votre profil et paramètres |

### Où se trouve le bouton de navigation ?

- **Desktop** : En haut à gauche de l'écran
- **Mobile** : Menu hamburger (☰) en haut à gauche

---

## Thèmes et abonnements

### Qu'est-ce qu'un thème ?

Un thème représente un sujet de programmation (JavaScript, Java, Python, Angular, etc.). 

**En s'abonnant à un thème**, vous verrez automatiquement dans votre fil d'actualité tous les articles associés à ce thème.

### Comment voir tous les thèmes disponibles ?

1. Ouvrez le menu (☰)
2. Cliquez sur **"THÈMES"**
3. Vous verrez la liste de tous les thèmes avec leur description

### Comment s'abonner à un thème ?

1. Allez dans la page **"THÈMES"**
2. Trouvez le thème qui vous intéresse
3. Cliquez sur le bouton **"S'ABONNER"**
4. Le bouton devient **"Déjà abonné"** et devient grisé
5. Les articles de ce thème apparaissent maintenant dans votre fil

**Important** : Vous devez être abonné à au moins un thème pour voir des articles dans votre fil.

### Comment se désabonner d'un thème ?

1. Allez dans votre **profil** (menu ☰ → ME)
2. Section **"Mes abonnements"** : liste de vos thèmes
3. Cliquez sur le bouton **"Se désabonner"** (🗑️) du thème concerné
4. Le thème est retiré de votre liste
5. Ses articles n'apparaissent plus dans votre fil

---

## Articles

### Comment voir les articles ?

1. Allez sur la page **"ARTICLES"** (fil d'actualité)
2. Les articles sont affichés du **plus récent au plus ancien** par défaut
3. Chaque carte d'article affiche :
   - Le titre
   - Le thème associé (badge coloré)
   - L'auteur
   - La date de publication
   - Un extrait du contenu

### Comment lire un article complet ?

1. Sur le fil d'actualité, cliquez sur une carte d'article
2. Vous accédez à la page de l'article avec :
   - Le contenu complet
   - Les informations de publication
   - Tous les commentaires
   - Un formulaire pour commenter

### Comment créer un article ?

1. Sur le fil d'actualité, cliquez sur le bouton **"Créer un article"**
2. Remplissez le formulaire :
   - **Thème** : Sélectionnez dans la liste déroulante
   - **Titre** : 3 caractères minimum
   - **Contenu** : 10 caractères minimum
3. Cliquez sur **"PUBLIER"**
4. Vous êtes redirigé vers le fil où votre article apparaît en premier

**Automatique** : 
- Votre nom d'utilisateur est ajouté comme auteur
- La date de publication est l'instant présent

### Puis-je modifier ou supprimer un article ?

❌ **Non**, dans cette version MVP (Minimum Viable Product), il n'est pas possible de :
- Modifier un article publié
- Supprimer un article

**Astuce** : Relisez bien votre article avant de publier !

---

## Commentaires

### Comment commenter un article ?

1. Ouvrez l'article (cliquez dessus dans le fil)
2. Descendez en bas de la page
3. Remplissez le champ **"Ajouter un commentaire"**
4. Cliquez sur **"ENVOYER"**
5. Votre commentaire apparaît instantanément sous l'article

**Automatique** :
- Votre nom d'utilisateur est ajouté
- La date/heure du commentaire est enregistrée

### Puis-je répondre à un commentaire ?

❌ **Non**, dans cette version, les commentaires ne sont pas récursifs.  
Vous pouvez uniquement commenter l'article principal, pas un autre commentaire.

### Puis-je modifier ou supprimer un commentaire ?

❌ **Non**, dans cette version MVP, les commentaires ne peuvent pas être modifiés ou supprimés après publication.

---

## Profil

### Comment accéder à mon profil ?

1. Ouvrez le menu (☰)
2. Cliquez sur **"ME"**
3. Vous accédez à votre page de profil

### Que contient ma page de profil ?

Votre profil affiche :

1. **Informations personnelles**
   - Nom d'utilisateur
   - Email
   - Date d'inscription

2. **Mes abonnements**
   - Liste des thèmes auxquels vous êtes abonné
   - Bouton pour se désabonner de chaque thème

### Comment modifier mon profil ?

1. Allez sur votre **profil** (ME)
2. Cliquez sur le bouton **"Modifier"** (icône crayon)
3. Les champs deviennent modifiables :
   - **Nom d'utilisateur**
   - **Email**
4. Faites vos modifications
5. Cliquez sur **"Sauvegarder"**
6. Vos informations sont mises à jour

**Note** : Le mot de passe ne peut pas être modifié dans cette version.

---

## Erreurs courantes

### ❌ "Cet email est déjà utilisé"

**Cause** : L'adresse email que vous essayez d'utiliser existe déjà dans la base.

**Solutions** :
1. Utilisez une autre adresse email
2. Si c'est votre email, connectez-vous au lieu de vous inscrire
3. Utilisez le format complet : `nom@domaine.com`

---

### ❌ "Ce nom d'utilisateur est déjà pris"

**Cause** : Le nom d'utilisateur existe déjà.

**Solutions** :
1. Choisissez un nom d'utilisateur différent
2. Ajoutez des chiffres ou caractères : `devjohn` → `devjohn123`
3. Minimum 3 caractères requis

---

### ❌ "Mot de passe invalide" (lors de l'inscription)

**Cause** : Votre mot de passe ne respecte pas les critères de sécurité.

**Solution** : Créez un mot de passe avec :
- ✅ Au moins 8 caractères
- ✅ Au moins 1 chiffre (0-9)
- ✅ Au moins 1 lettre minuscule (a-z)
- ✅ Au moins 1 lettre majuscule (A-Z)
- ✅ Au moins 1 caractère spécial (!, @, #, $, %, etc.)

**Exemples valides** :
- `MonMotDePasse123!`
- `SuperDev@2024`
- `Angular#14Test`

---

### ❌ "Identifiants invalides" (lors de la connexion)

**Causes possibles** :
1. Email ou nom d'utilisateur incorrect
2. Mot de passe incorrect
3. Compte inexistant

**Solutions** :
1. Vérifiez l'orthographe de votre email/username
2. Vérifiez votre mot de passe (attention aux majuscules)
3. Si vous n'avez pas de compte, inscrivez-vous d'abord
4. Essayez avec votre email **OU** votre nom d'utilisateur

---

### ❌ "Erreur lors du chargement des articles"

**Causes possibles** :
1. Vous n'êtes abonné à aucun thème
2. Problème de connexion au serveur

**Solutions** :
1. Abonnez-vous à au moins un thème (THÈMES → S'ABONNER)
2. Vérifiez que le backend est démarré (`http://localhost:8081`)
3. Rafraîchissez la page (F5)

---

### ❌ La page ne charge pas / Erreur 404

**Causes** :
1. Le frontend n'est pas démarré
2. Mauvaise URL

**Solutions** :
1. Vérifiez que le serveur Angular est démarré :
   ```bash
   cd front
   npm start
   ```
2. Utilisez l'URL correcte : `http://localhost:4200`
3. Attendez le message : `Angular Live Development Server is listening on localhost:4200`

---

### ❌ "Utilisateur non trouvé" (lors d'une modification de profil)

**Cause** : Votre session a expiré (token JWT > 24h).

**Solution** :
1. Déconnectez-vous
2. Reconnectez-vous
3. Réessayez la modification

---

## Sécurité

### Mes données sont-elles sécurisées ?

✅ **Oui**, MDD utilise plusieurs mécanismes de sécurité :

1. **Mots de passe** : Hashés avec **BCrypt** (jamais stockés en clair)
2. **Authentification** : Tokens **JWT** (JSON Web Token) avec expiration 24h
3. **Communication** : API REST sécurisée (headers Authorization)
4. **Validation** : Tous les formulaires sont validés côté client ET serveur

### Combien de temps ma session reste-t-elle active ?

- **Durée** : 24 heures
- **Après 24h** : Vous devez vous reconnecter
- **Si vous fermez le navigateur** : Vous restez connecté (localStorage)

### Puis-je utiliser l'application sur mobile ?

✅ **Oui**, MDD est responsive et s'adapte automatiquement :
- 📱 **Smartphone** : Interface optimisée mobile
- 💻 **Tablette** : Affichage adapté
- 🖥️ **Desktop** : Pleine résolution

---

## Support et Contact

### J'ai un problème technique, que faire ?

1. Consultez d'abord cette FAQ
2. Vérifiez que le backend ET frontend sont démarrés
3. Regardez la console du navigateur (F12) pour les erreurs
4. Contactez l'équipe de développement ORION

### Où signaler un bug ?

Créez une issue sur le repository GitHub du projet avec :
- Description du problème
- Étapes pour reproduire
- Captures d'écran si possible
- Console d'erreur (F12)

---

## Fonctionnalités futures

### Quelles améliorations sont prévues ?

Pour les versions futures (hors MVP) :
- ✨ Modification/suppression d'articles et commentaires
- ✨ Réponses aux commentaires (commentaires récursifs)
- ✨ Système de likes/votes
- ✨ Notifications en temps réel
- ✨ Messages privés entre utilisateurs
- ✨ Recherche d'articles et d'utilisateurs
- ✨ Upload d'images dans les articles
- ✨ Thèmes personnalisés (dark mode)
- ✨ Statistiques de profil

---

**Version** : 1.0.0 (MVP)  
**Dernière mise à jour** : Décembre 2025  
**Équipe** : ORION - Monde de Dév
