package org.esfe.UsuarioApi.seguridad.servicios;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    // Usando ConcurrentHashMap para thread-safety
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    /**
     * Añade un token a la lista negra
     * @param token Token a invalidar
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    /**
     * Verifica si un token está en la lista negra
     * @param token Token a verificar
     * @return true si está en la lista negra, false en caso contrario
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    /**
     * Limpia tokens expirados de la lista negra
     * Este método debería ejecutarse periódicamente
     */
    public void cleanExpiredTokens() {
        // Aquí podrías implementar lógica para limpiar tokens expirados
        // Por simplicidad, no lo implemento ahora, pero sería recomendable
        // tener un job que limpie la lista periódicamente
    }

    /**
     * Obtiene el tamaño actual de la lista negra (para debugging)
     */
    public int getBlacklistSize() {
        return blacklistedTokens.size();
    }
}