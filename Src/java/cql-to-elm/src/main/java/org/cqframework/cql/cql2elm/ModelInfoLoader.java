package org.cqframework.cql.cql2elm;

import org.hl7.elm.r1.VersionedIdentifier;

import java.util.HashMap;
import java.util.Map;

public class ModelInfoLoader {

    private static final Map<VersionedIdentifier, ModelInfoProvider> PROVIDERS =
            new HashMap<VersionedIdentifier, ModelInfoProvider>();

    static {
        registerModelInfoProvider(new VersionedIdentifier().withId("System").withVersion("1"), new SystemModelInfoProvider());
        registerModelInfoProvider(new VersionedIdentifier().withId("QUICK").withVersion("1"), new QuickModelInfoProvider());
        //registerModelInfoProvider(new VersionedIdentifier().withId("ADL").withVersion("1"), new AdlModelInfoProvider());
        registerModelInfoProvider(new VersionedIdentifier().withId("QDM").withVersion("4.1.2"), new QdmModelInfoProvider());
        registerModelInfoProvider(new VersionedIdentifier().withId("FHIR").withVersion("1.4"), new FhirModelInfoProvider());
    }

    public static ModelInfoProvider getModelInfoProvider(VersionedIdentifier modelIdentifier) {
        checkModelIdentifier(modelIdentifier);

        ModelInfoProvider provider = PROVIDERS.get(modelIdentifier);
        if (provider == null) {
            throw new IllegalArgumentException(String.format("Could not resolve model info provider for model %s, version %s.",
                    modelIdentifier.getId(), modelIdentifier.getVersion()));
        }

        return provider;
    }

    public static void registerModelInfoProvider(VersionedIdentifier modelIdentifier, ModelInfoProvider provider) {
        checkModelIdentifier(modelIdentifier);

        if (provider == null) {
            throw new IllegalArgumentException("Provider is null");
        }

        PROVIDERS.put(modelIdentifier, provider);

        if (modelIdentifier.getVersion() != null) {
            VersionedIdentifier versionlessIdentifier = new VersionedIdentifier().withId(modelIdentifier.getId());
            PROVIDERS.put(versionlessIdentifier, provider);
        }
    }

    public static void unregisterModelInfoProvider(VersionedIdentifier modelIdentifier) {
        checkModelIdentifier(modelIdentifier);

        PROVIDERS.remove(modelIdentifier);
    }

    private static void checkModelIdentifier(VersionedIdentifier modelIdentifier) {
        if (modelIdentifier == null) {
            throw new IllegalArgumentException("modelIdentifier is null.");
        }

        if (modelIdentifier.getId() == null || modelIdentifier.getId().equals("")) {
            throw new IllegalArgumentException("modelIdentifier Id is null.");
        }
    }
}
