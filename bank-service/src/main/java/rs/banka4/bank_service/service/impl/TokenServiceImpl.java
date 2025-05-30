package rs.banka4.bank_service.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.banka4.bank_service.domain.auth.db.Token;
import rs.banka4.bank_service.repositories.TokenRepository;
import rs.banka4.bank_service.service.abstraction.TokenService;
import rs.banka4.rafeisen.common.exceptions.jwt.RefreshTokenRevoked;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Override
    public void invalidateToken(String token) {
        Token invalidatedToken =
            Token.builder()
                .token(token)
                .build();
        Optional<Token> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isPresent()) {
            throw new RefreshTokenRevoked();
        }

        tokenRepository.save(invalidatedToken);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
}
