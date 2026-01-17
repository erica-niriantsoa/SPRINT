<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Afficher Session - Sprint 11</title>
</head>
<body>
    <div class="container">
        <h1> Contenu de la Session</h1>
        
        <div class="info-box">
            <strong>SPRINT 11 - Gestion de Session</strong><br>
            Cette page affiche toutes les donnees stockees dans votre session HTTP,
            converties en Map&lt;String, Object&gt; grâce à l'annotation @Session.
        </div>
        
        <%
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
            
            if (sessionData != null && !sessionData.isEmpty()) {
        %>
            <table class="session-table">
                <thead>
                    <tr>
                        <th>Cle</th>
                        <th>Valeur</th>
                        <th>Type</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    for (Map.Entry<String, Object> entry : sessionData.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        String type = value != null ? value.getClass().getSimpleName() : "null";
                        String displayValue = value != null ? value.toString() : "null";
                        boolean isColor = displayValue.startsWith("#") && displayValue.length() == 7;
                %>
                    <tr>
                        <td class="key"><%= key %></td>
                        <td class="value">
                            <% if (isColor) { %>
                                <div style="display: flex; align-items: center; gap: 10px;">
                                    <div style="width: 30px; height: 30px; background-color: <%= displayValue %>; border: 2px solid #333; border-radius: 4px;"></div>
                                    <span><%= displayValue %></span>
                                </div>
                            <% } else { %>
                                <%= displayValue %>
                            <% } %>
                        </td>
                        <td><em><%= type %></em></td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
            
            <p><strong>Total :</strong> <%= sessionData.size() %> element(s) en session</p>
        <%
            } else {
        %>
            <div class="empty-message">
                 La session est vide.<br>
                Aucune donnee n'est actuellement stockee.
            </div>
        <%
            }
        %>
        
        <div class="actions">
            <a href="<%= request.getContextPath() %>/session/choix-couleur">🎨 Choisir une couleur</a>
            <a href="<%= request.getContextPath() %>/session/afficher" class="secondary">🔄 Rafraîchir</a>
            <a href="<%= request.getContextPath() %>/session/vider" class="danger">🗑️ Vider la session</a>
            <a href="<%= request.getContextPath() %>/" class="secondary">🏠 Accueil</a>
        </div>
    </div>
</body>
</html>
