package org.contextmapper.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VersionProviderTest {

    static class TestableVersionProvider extends VersionProvider {
        private final Package mockPackage;

        TestableVersionProvider(Package mockPackage) {
            this.mockPackage = mockPackage;
        }

        @Override
        protected Package getPackageToInspect() {
            return mockPackage;
        }
    }

    @Test
    void getVersion_whenImplVersionIsPresent_returnsVersionString() throws Exception {
        // Arrange
        String expectedVersion = "1.2.3";
        Package mockPackage = Mockito.mock(Package.class);
        Mockito.when(mockPackage.getImplementationVersion()).thenReturn(expectedVersion);
        VersionProvider versionProvider = new TestableVersionProvider(mockPackage);

        // Act
        String[] version = versionProvider.getVersion();

        // Assert
        assertThat(version).isNotNull()
                           .hasSize(1)
                           .containsExactly("Context Mapper CLI v" + expectedVersion);
    }

    @Test
    void getVersion_whenImplVersionIsNull_returnsDevelopmentVersionString() throws Exception {
        // Arrange
        Package mockPackage = Mockito.mock(Package.class);
        Mockito.when(mockPackage.getImplementationVersion()).thenReturn(null);
        VersionProvider versionProvider = new TestableVersionProvider(mockPackage);

        // Act
        String[] version = versionProvider.getVersion();

        // Assert
        assertThat(version).isNotNull()
                           .hasSize(1)
                           .containsExactly("Context Mapper CLI DEVELOPMENT VERSION");
    }

    @Test
    void getVersion_whenPackageIsNull_returnsDevelopmentVersionString() throws Exception {
        // Arrange
        VersionProvider versionProvider = new TestableVersionProvider(null);

        // Act
        String[] version = versionProvider.getVersion();

        // Assert
        assertThat(version).isNotNull()
                           .hasSize(1)
                           .containsExactly("Context Mapper CLI DEVELOPMENT VERSION");
    }
} 