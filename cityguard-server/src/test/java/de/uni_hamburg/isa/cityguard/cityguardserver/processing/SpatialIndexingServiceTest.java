package de.uni_hamburg.isa.cityguard.cityguardserver.processing;

import com.uber.h3core.H3Core;
import com.uber.h3core.util.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Slf4j
public class SpatialIndexingServiceTest {


    @Test
    public void h3Test() {
        try {
            H3Core h3 = H3Core.newInstance();
            double lat = 37.775938728915946;
            double lng = -122.41795063018799;
            int res = 9;

            String hexAddr = h3.latLngToCellAddress(lat, lng, res);
            log.info("hexAddr: {}", hexAddr);
            List<LatLng> LatLngs = h3.cellToBoundary(hexAddr);
            log.info("LatLngs: {}", LatLngs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
