package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.PrintPriceDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.PrintPrice;
import org.photography.api.repository.PrintPriceRepository;
import org.photography.api.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrintPriceService {

    private final PrintPriceRepository printPriceRepository;

    @Autowired
    public PrintPriceService(PrintPriceRepository printPriceRepository) {
        this.printPriceRepository = printPriceRepository;
    }

    @Autowired
    private ModelMapper modelMapper;

    public PrintPriceDTO createPrintPrice(PrintPriceDTO printPriceDTO) {
        ValidationUtils.validateName(printPriceDTO.getFormat());

        PrintPrice printPriceToCreate = modelMapper.map(printPriceDTO, PrintPrice.class);

        PrintPrice createdPrintPrice = printPriceRepository.save(printPriceToCreate);

        return modelMapper.map(createdPrintPrice, PrintPriceDTO.class);
    }

    public PrintPriceDTO getPrintPriceById(Long id) {
        ValidationUtils.validateId(id);

        PrintPrice printPrice = printPriceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PrintPrice", id));

        return modelMapper.map(printPrice, PrintPriceDTO.class);
    }

    public List<PrintPriceDTO> getAllPrintPrices() {
        List<PrintPrice> printPrices = printPriceRepository.findAll();

        return printPrices.stream()
                .map(printPrice -> modelMapper.map(printPrice, PrintPriceDTO.class))
                .collect(Collectors.toList());
    }

    public PrintPriceDTO updatePrintPrice(Long id, PrintPriceDTO updatedPrintPriceDTO) {
        ValidationUtils.validateId(id);
        ValidationUtils.validateName(updatedPrintPriceDTO.getFormat());

        PrintPrice existingPrintPrice = printPriceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PrintPrice", id));

        existingPrintPrice.setFormat(updatedPrintPriceDTO.getFormat());
        existingPrintPrice.setPaperPrice(updatedPrintPriceDTO.getPaperPrice());
        existingPrintPrice.setDibondPrice(updatedPrintPriceDTO.getDibondPrice());

        PrintPrice updatedPrintPrice = printPriceRepository.save(existingPrintPrice);

        return modelMapper.map(updatedPrintPrice, PrintPriceDTO.class);
    }

    public void deletePrintPrice(Long id) {
        ValidationUtils.validateId(id);

        if (printPriceRepository.existsById(id)) {
            printPriceRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("PrintPrice", id);
        }
    }

}
