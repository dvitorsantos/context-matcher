package lsdi.Services;

import lsdi.Entities.EventProcessNetwork;
import lsdi.Entities.Rule;
import lsdi.Repositories.EventProcessNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventProcessNetworkService {
    @Autowired
    EventProcessNetworkRepository eventProcessNetworkRepository;

    public Optional<EventProcessNetwork> findById(String id) {
        return eventProcessNetworkRepository.findById(id);
    }

    public List<EventProcessNetwork> findAll() {
        return eventProcessNetworkRepository.findAll();
    }

    public List<EventProcessNetwork> findAllByMatched(boolean matched) {
        return eventProcessNetworkRepository.findAllByMatched(matched);
    }

    public List<EventProcessNetwork> findAllByNode(String nodeUuid) {
        return eventProcessNetworkRepository.findAllByHost(nodeUuid);
    }
    public EventProcessNetwork save(EventProcessNetwork eventProcessNetwork) {
        return eventProcessNetworkRepository.save(eventProcessNetwork);
    }
}
