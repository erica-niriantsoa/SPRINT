<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Choisir une Couleur - Sprint 11</title>
    <script>
        function selectColor(color, element) {
            document.querySelectorAll('.color-option').forEach(el => {
                el.classList.remove('selected');
            });
            element.classList.add('selected');
            document.getElementById('color-' + color).checked = true;
        }
    </script>
</head>
<body>
    <div class="container">
        <h1> Choisir votre Couleur Preferee</h1>
        
        <div class="info">
            <p><strong>Session Active</strong></p>
            <p>Nombre de visites : <strong>${nbVisites}</strong></p>
            <% if (request.getAttribute("couleurActuelle") != null) { %>
                <p>Couleur actuelle : <strong>${couleurActuelle}</strong></p>
            <% } else { %>
                <p><em>Aucune couleur n'est encore enregistree en session</em></p>
            <% } %>
        </div>
        
        <form method="post" action="${pageContext.request.contextPath}/session/enregistrer-couleur">
            <div class="color-grid">
                <label class="color-option" style="background-color: #FF6B6B;" onclick="selectColor('rouge', this)">
                    <input type="radio" name="couleur" id="color-rouge" value="#FF6B6B">
                    Rouge
                </label>
                
                <label class="color-option" style="background-color: #4ECDC4;" onclick="selectColor('turquoise', this)">
                    <input type="radio" name="couleur" id="color-turquoise" value="#4ECDC4">
                    Turquoise
                </label>
                
                <label class="color-option" style="background-color: #45B7D1;" onclick="selectColor('bleu', this)">
                    <input type="radio" name="couleur" id="color-bleu" value="#45B7D1">
                    Bleu
                </label>
                
                <label class="color-option" style="background-color: #96CEB4;" onclick="selectColor('vert', this)">
                    <input type="radio" name="couleur" id="color-vert" value="#96CEB4">
                    Vert
                </label>
                
                <label class="color-option" style="background-color: #FFEAA7;" onclick="selectColor('jaune', this)">
                    <input type="radio" name="couleur" id="color-jaune" value="#FFEAA7">
                    <span style="color: #333;">Jaune</span>
                </label>
                
                <label class="color-option" style="background-color: #DFE6E9;" onclick="selectColor('gris', this)">
                    <input type="radio" name="couleur" id="color-gris" value="#DFE6E9">
                    <span style="color: #333;">Gris</span>
                </label>
                
                <label class="color-option" style="background-color: #FD79A8;" onclick="selectColor('rose', this)">
                    <input type="radio" name="couleur" id="color-rose" value="#FD79A8">
                    Rose
                </label>
                
                <label class="color-option" style="background-color: #A29BFE;" onclick="selectColor('violet', this)">
                    <input type="radio" name="couleur" id="color-violet" value="#A29BFE">
                    Violet
                </label>
            </div>
            
            <button type="submit"> Enregistrer ma Couleur</button>
        </form>
        
        <div class="actions">
            <a href="<%= request.getContextPath() %>/session/afficher">Voir toute la session</a>
            <a href="<%= request.getContextPath() %>/session/vider" class="danger"> Vider la session</a>
            <a href="<%= request.getContextPath() %>/"> Accueil</a>
        </div>
    </div>
</body>
</html>
