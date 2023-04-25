package academy.devdojo.springbootessentials.controller;

import academy.devdojo.springbootessentials.domain.Cartoon;
import academy.devdojo.springbootessentials.requests.CartoonPostRequestBody;
import academy.devdojo.springbootessentials.requests.CartoonPutRequestBody;
import academy.devdojo.springbootessentials.service.CartoonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cartoons")
@Log4j2
@RequiredArgsConstructor
public class CartoonController {
    private final CartoonService cartoonService;

//    @GetMapping("/oi")
//    public String list() {
//        return "oi";
//    }

    @GetMapping
    @Operation(summary = "List all cartoons paginated", description = "The default size is 20, use the parameter size to change the default value",
            tags = {"cartoon"})
    public ResponseEntity<Page<Cartoon>> list(@ParameterObject Pageable pegeable) {
        return ResponseEntity.ok(cartoonService.listAll(pegeable));
    }


    @GetMapping(path = "/all")
    public ResponseEntity<List<Cartoon>> listAll() {
        return ResponseEntity.ok(cartoonService.listAllNonPageable());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Cartoon> findById (@PathVariable long id) {
        return ResponseEntity.ok(cartoonService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "by-id/{id}")
    public ResponseEntity<Cartoon> findByIdAuthenticationPrincipal (@PathVariable long id,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        log.info(userDetails);
        return ResponseEntity.ok(cartoonService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Cartoon>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(cartoonService.findByName(name));
    }


    @PostMapping
    public ResponseEntity<Cartoon> save(@RequestBody @Valid CartoonPostRequestBody cartoonPostRequestBody) {
        return new ResponseEntity<>(cartoonService.save(cartoonPostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/admin/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Anime Does Not Exist in The Database"),
    })
    public ResponseEntity<Void> delete(@PathVariable long id) {
        cartoonService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody CartoonPutRequestBody cartoonPutRequestBody) {
        cartoonService.replace(cartoonPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);


    }
}