# Tests de compatibilité responsive - MDD

**Date** : 6 janvier 2026  
**Testeur** : Équipe de développement  
**Environnement** : Chrome DevTools

---

## 1. Résolutions testées

| Appareil | Résolution | Type | Orientation |
|----------|-----------|------|-------------|
| iPhone 12 Pro | 390 x 844 | Mobile | Portrait |
| Samsung Galaxy S20 | 360 x 800 | Mobile | Portrait |
| iPad Air | 820 x 1180 | Tablet | Portrait |
| iPad Air | 1180 x 820 | Tablet | Paysage |

---

## 2. Pages à tester

### ✅ Page d'accueil (Home)
- [ ] Navigation visible et accessible
- [ ] Boutons "S'inscrire" et "Se connecter" bien placés
- [ ] Texte lisible sans zoom
- [ ] Images responsive

### ✅ Page d'inscription (Register)
- [ ] Formulaire utilisable sur mobile
- [ ] Champs de saisie accessibles au clavier mobile
- [ ] Validation en temps réel fonctionnelle
- [ ] Bouton "S'inscrire" accessible
- [ ] Pas de débordement horizontal

### ✅ Page de connexion (Login)
- [ ] Formulaire centré et lisible
- [ ] Champs email/password bien dimensionnés
- [ ] Bouton de connexion accessible
- [ ] Messages d'erreur visibles

### ✅ Fil d'actualité (Feed)
- [ ] Header navigation responsive (burger menu si nécessaire)
- [ ] Liste d'articles scrollable
- [ ] Cartes d'articles bien formatées
- [ ] Tri par date visible
- [ ] Bouton "Créer un article" accessible

### ✅ Détail d'article (Article)
- [ ] Contenu article lisible
- [ ] Informations auteur/thème/date visibles
- [ ] Formulaire commentaire accessible
- [ ] Liste commentaires scrollable
- [ ] Bouton retour fonctionnel

### ✅ Création d'article (Article Create)
- [ ] Formulaire de création accessible
- [ ] Sélecteur de thème utilisable
- [ ] Champs titre/contenu bien dimensionnés
- [ ] Bouton "Publier" visible
- [ ] Validation des champs fonctionnelle

### ✅ Liste des thèmes (Themes)
- [ ] Grille/liste de thèmes adaptée
- [ ] Boutons "S'abonner" accessibles
- [ ] Descriptions lisibles
- [ ] Scroll fluide

### ✅ Profil utilisateur (Profile)
- [ ] Informations utilisateur lisibles
- [ ] Formulaire d'édition accessible
- [ ] Liste des abonnements visible
- [ ] Boutons "Modifier" / "Sauvegarder" accessibles
- [ ] Chips des thèmes adaptés

---

## 3. Critères d'acceptation globaux

### Navigation
- [ ] Pas de débordement horizontal (overflow-x)
- [ ] Menu de navigation accessible
- [ ] Liens et boutons suffisamment grands (min 44x44px)
- [ ] Touch targets espacés (éviter clics accidentels)

### Contenu
- [ ] Texte lisible sans zoom (min 16px)
- [ ] Images responsive (max-width: 100%)
- [ ] Pas de contenu masqué
- [ ] Scroll vertical fluide

### Formulaires
- [ ] Champs de saisie bien dimensionnés
- [ ] Labels visibles
- [ ] Validation en temps réel fonctionnelle
- [ ] Clavier mobile adapté (email, number, etc.)

### Performance
- [ ] Temps de chargement acceptable (<3s)
- [ ] Transitions fluides
- [ ] Pas de lag au scroll

---

## 4. Instructions de test

### Méthode DevTools Chrome
1. Ouvrir Chrome DevTools (F12)
2. Activer le mode Device Toolbar (Ctrl+Shift+M)
3. Sélectionner l'appareil dans la liste déroulante
4. Tester chaque page en naviguant dans l'application
5. Noter les observations

### Commandes pour lancer l'application
```bash
# Terminal 1 - Backend
cd back
set DB_USER=mdd_user
set DB_PASSWORD=mdd_password
mvnw.cmd spring-boot:run

# Terminal 2 - Frontend
cd front
npm start
```

### URL de test
http://localhost:4200

---

## 5. Observations et corrections

### 📝 Notes de test
_(À compléter pendant les tests)_

**Mobile (iPhone 12 Pro - 390x844)** :
- [ ] Home : 
- [ ] Register : 
- [ ] Login : 
- [ ] Feed : 
- [ ] Article : 
- [ ] Article Create : 
- [ ] Themes : 
- [ ] Profile : 

**Mobile (Galaxy S20 - 360x800)** :
- [ ] Home : 
- [ ] Register : 
- [ ] Login : 
- [ ] Feed : 
- [ ] Article : 
- [ ] Article Create : 
- [ ] Themes : 
- [ ] Profile : 

**Tablet (iPad Air - Portrait)** :
- [ ] Home : 
- [ ] Register : 
- [ ] Login : 
- [ ] Feed : 
- [ ] Article : 
- [ ] Article Create : 
- [ ] Themes : 
- [ ] Profile : 

**Tablet (iPad Air - Paysage)** :
- [ ] Home : 
- [ ] Register : 
- [ ] Login : 
- [ ] Feed : 
- [ ] Article : 
- [ ] Article Create : 
- [ ] Themes : 
- [ ] Profile : 

### 🐛 Problèmes identifiés
_(À documenter si nécessaire)_

1. **Problème** : 
   - **Appareil** : 
   - **Page** : 
   - **Description** : 
   - **Sévérité** : Critique / Moyenne / Faible
   - **Correctif** : 

### ✅ Points validés
_(À compléter avec les succès)_

---

## 6. Conclusion

### Résumé des résultats
- **Pages testées** : 0/8
- **Appareils testés** : 0/4
- **Problèmes critiques** : 0
- **Problèmes moyens** : 0
- **Problèmes mineurs** : 0

### Recommandations
_(À compléter après les tests)_

### Statut final
- [ ] ✅ Application 100% responsive
- [ ] ⚠️ Quelques ajustements nécessaires
- [ ] ❌ Corrections majeures requises

---

**Note** : Ce document sera utilisé pour documenter la compatibilité responsive dans la section 3.2 (Rapport de performance et optimisation) de Documentation_MDD_Officielle.md.
