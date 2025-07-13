# Gestion d'une Bibliothèque

---

## Fonctionnalité 1 : Prêter un livre

- **Acteur principal** : Bibliothécaire
- **Entrées** :
  - ID de l’adhérent
  - ID de l’exemplaire
  - Type de prêt (lecture sur place ou à domicile)

### Scénario nominal

Le bibliothécaire sélectionne un adhérent valide et un exemplaire disponible, puis enregistre le prêt.

### Règles de gestion

1. L’adhérent existe dans la base de données.
2. L’adhérent est abonné.
3. L’adhérent est actif.
4. L’adhérent n’est pas sanctionné.
5. L’exemplaire est disponible.
6. Le type de prêt est compatible avec les règles du livre.
7. Le contenu du livre est adapté à l’âge de l’adhérent (ex : livre +18 interdit aux mineurs).
8. Le nombre de prêts à domicile en cours ne dépasse pas le quota autorisé selon le profil de l’adhérent.

### Scénarios alternatifs

- Adhérent inexistant
- Adhérent non abonné
- Adhérent inactif
- Adhérent sanctionné
- Exemplaire indisponible
- Type de prêt non autorisé
- Âge non conforme
- Quota dépassé

### Résultat attendu

- ✅ Le prêt est enregistré avec succès.
- ❌ Un message d’erreur est affiché si une règle est violée.

---

## Fonctionnalité 2 : Prolonger un prêt

- **Acteurs** :
  - Adhérent (effectue la demande)
  - Bibliothécaire (valide ou rejette)
- **Entrée** : ID du prêt concerné

### Scénario nominal

L’adhérent demande la prolongation d’un prêt. Le bibliothécaire analyse la demande et la valide ou la rejette.

### Règles de gestion

1. L’adhérent ne doit pas être sanctionné.
2. Le nombre de prolongations en cours ne doit pas dépasser le maximum autorisé selon le profil de l’adhérent.

### Scénarios alternatifs

- Adhérent sanctionné
- Nombre de prolongations autorisées dépassé

### Résultat attendu

- ✅ La prolongation est enregistrée.
- ❌ La demande est rejetée si les conditions ne sont pas remplies.

---

## Fonctionnalité 3 : Rendre un livre

- **Acteur** : Bibliothécaire

### Scénario nominal

L’adhérent restitue le livre au bibliothécaire, qui enregistre le retour dans le système.

### Règles de gestion

1. Si la **date de retour est postérieure à la date de fin de prêt** :
   - Une sanction est appliquée selon la durée de retard et le profil de l’adhérent.
2. Si la **date de fin du prêt tombe un jour férié ou un week-end** :
   - Un **paramètre système** détermine si le retour est attendu avant ou après ce jour.
   - Si l’adhérent rend le livre **au-delà du premier jour ouvrable suivant**, une sanction est appliquée.

### Résultat attendu

- ✅ Le retour est enregistré.
- ⚠️ Une sanction est appliquée en cas de retard.

---

## Fonctionnalité 4 : Réserver un livre

- **Acteurs** :
  - Adhérent (formule la demande)
  - Bibliothécaire (valide ou rejette, puis transforme la réservation en prêt)

### Scénario nominal

1. L’adhérent consulte les exemplaires disponibles.
2. Il choisit un exemplaire et une date de réservation.
3. Le bibliothécaire valide ou rejette la demande.
4. À la date prévue, si l’exemplaire est toujours disponible, la réservation est convertie en prêt.

### Règles de gestion

1. Le nombre de réservations actives (non encore transformées en prêt) ne doit pas dépasser le quota autorisé selon le profil de l’adhérent.
2. Toutes les règles applicables à un prêt classique doivent être respectées :
   - Adhérent actif, non sanctionné, abonné, quota de prêt respecté, etc.

### Résultat attendu

- ✅ Réservation validée et transformée en prêt à la date prévue.
- ❌ Réservation rejetée si une règle n’est pas respectée.
