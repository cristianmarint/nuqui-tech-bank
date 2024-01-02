package co.nuqui.tech.mshumans.infrastructure.controller;

import co.nuqui.tech.mshumans.domain.dto.Human;
import co.nuqui.tech.mshumans.domain.service.HumanService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static co.nuqui.tech.mshumans.infrastructure.controller.Mappings.URL_HUMANS_V1;

@RestController
@RequestMapping(URL_HUMANS_V1)
@AllArgsConstructor
public class HumanController {
    private final HumanService humanService;

    @ExceptionHandler(GlobalException.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/save")
    public ResponseEntity<Human> create(@RequestBody Human human) {
        return ResponseEntity.ok().body(humanService.save(human));
    }

    @ExceptionHandler(GlobalException.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/search/")
    public ResponseEntity<Human> searchByIdentification(
            @Param("identification") String identification,
            @Param("id") Long id
    ) {
        return ResponseEntity.ok().body(humanService.findByIdentificationOrId(identification,id));
    }
}
