package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.TicketList;
import lombok.NonNull;
import java.io.File;
import java.io.IOException;

public class JsonTicketParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TicketList parseTickets(@NonNull String path) throws IOException {
        File file = new File(path);
        return objectMapper.readValue(file, TicketList.class);
    }
}
