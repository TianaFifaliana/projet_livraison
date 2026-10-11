package com.example.livraison.dto.request;

import jakarta.validation.groups.Default;

public final class ValidationGroups {

    private ValidationGroups() {
    }

    public interface Api extends Default {
    }

    public interface Web extends Default {
    }
}
