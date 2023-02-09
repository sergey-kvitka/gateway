package com.kvitka.gateway.controllers;

import com.kvitka.gateway.dtos.FinishRegistrationRequestDTO;
import com.kvitka.gateway.dtos.LoanApplicationRequestDTO;
import com.kvitka.gateway.dtos.LoanOfferDTO;
import com.kvitka.gateway.services.impl.RestTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.kvitka.gateway.util.UtilMethods.insertPathVariable;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ApiGatewayController {

    @Value("${rest.application.url}")
    private String applicationURL;
    @Value("${rest.application.endpoints.application}")
    private String applicationEndpoint;
    @Value("${rest.application.endpoints.offer}")
    private String applicationOfferEndpoint;

    @Value("${rest.deal.url}")
    private String dealURL;
    @Value("${rest.deal.endpoints.calculate}")
    private String dealCalculateEndpoint;
    @Value("${rest.deal.endpoints.send}")
    private String dealSendEndpoint;
    @Value("${rest.deal.endpoints.sign}")
    private String dealSignEndpoint;
    @Value("${rest.deal.endpoints.code}")
    private String dealCodeEndpoint;

    private final RestTemplateService restTemplateService;

    @PostMapping("application")
    public List<LoanOfferDTO> application(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("[@PostMapping(application)] application method called. Argument: {}", loanApplicationRequestDTO);

        String applicationURL = this.applicationURL + '/' + applicationEndpoint;
        log.info("Sending POST request on \"{}\" (request body: {})", applicationURL, loanApplicationRequestDTO);
        ResponseEntity<LoanOfferDTO[]> loanOffersResponse = restTemplateService.postForEntity(
                applicationURL, loanApplicationRequestDTO, LoanOfferDTO[].class);
        log.info("POST request on \"{}\" sent successfully!", applicationURL);
        List<LoanOfferDTO> loanOffers = List.of(Objects.requireNonNull(loanOffersResponse.getBody()));
        log.info("Response from POST request on \"{}\" is: {}", applicationURL, loanOffers);

        log.info("[@PostMapping(application)] application method returns value: {}", loanOffers);
        return loanOffers;
    }

    @PutMapping("offer")
    public ResponseEntity<?> offer(@RequestBody LoanOfferDTO loanOfferDTO) {
        log.info("[@PutMapping(offer)] offer method called. Argument: {}", loanOfferDTO);
        String offerDealURL = applicationURL + '/' + applicationOfferEndpoint;
        log.info("Sending PUT request on \"{}\" (request body: {})", offerDealURL, loanOfferDTO);
        restTemplateService.put(offerDealURL, loanOfferDTO);
        log.info("PUT request on \"{}\" sent successfully!", offerDealURL);
        log.info("[@PutMapping(offer)] offer method finished.");
        return new ResponseEntity<>(loanOfferDTO.isEmpty() ? HttpStatus.ACCEPTED : HttpStatus.OK);
    }

    @PutMapping("calculate/{applicationId}")
    public void calculate(@PathVariable Long applicationId,
                          @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("[@PutMapping(calculate/{applicationId})] calculate method called. Argument: {}",
                finishRegistrationRequestDTO);
        String dealCalculateURL = dealURL + '/' +
                insertPathVariable(dealCalculateEndpoint, "applicationId", applicationId);
        log.info("Sending PUT request on \"{}\" (request body: {})", dealCalculateURL, finishRegistrationRequestDTO);
        restTemplateService.put(dealCalculateURL, finishRegistrationRequestDTO);
        log.info("PUT request on \"{}\" sent successfully!", dealCalculateURL);
        log.info("[@PutMapping(calculate/{applicationId})] calculate method finished.");
    }

    @PutMapping("document/{applicationId}/send")
    public void documentSend(@PathVariable Long applicationId) {
        log.info("[@PutMapping(document/{applicationId}/send)] documentSend method called. Argument: {}",
                applicationId);
        String documentSendURL = dealURL + '/' +
                insertPathVariable(dealSendEndpoint, "applicationId", applicationId);
        log.info("Sending PUT request on \"{}\"", documentSendURL);
        restTemplateService.put(documentSendURL);
        log.info("PUT request on \"{}\" sent successfully!", documentSendURL);
        log.info("[@PutMapping(document/{applicationId}/send)] documentSend method finished.");
    }

    @PutMapping("document/{applicationId}/sign")
    public void documentSign(@PathVariable Long applicationId) {
        log.info("[@PutMapping(document/{applicationId}/sign)] documentSign method called. Argument: {}",
                applicationId);
        String documentSignURL = dealURL + '/' +
                insertPathVariable(dealSignEndpoint, "applicationId", applicationId);
        log.info("Sending PUT request on \"{}\"", documentSignURL);
        restTemplateService.put(documentSignURL);
        log.info("PUT request on \"{}\" sent successfully!", documentSignURL);
        log.info("[@PutMapping(document/{applicationId}/sign)] documentSign method finished.");
    }

    @PutMapping("document/{applicationId}/code")
    public void documentCode(@PathVariable Long applicationId, @RequestBody Integer sesCodeToVerify) {
        log.info("[@PutMapping(document/{applicationId}/code)] documentCode method called. Argument: {}",
                applicationId);
        String documentCodeURL = dealURL + '/' +
                insertPathVariable(dealCodeEndpoint, "applicationId", applicationId);
        log.info("Sending PUT request on \"{}\"", documentCodeURL);
        restTemplateService.put(documentCodeURL, sesCodeToVerify);
        log.info("PUT request on \"{}\" sent successfully!", documentCodeURL);
        log.info("[@PutMapping(document/{applicationId}/code)] documentCode method finished.");
    }
}
