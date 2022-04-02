import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ResourceBoundary {
    @Getter
    private final String projectId;
    @Getter
    private final String region;
}
