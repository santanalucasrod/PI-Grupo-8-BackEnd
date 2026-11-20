
package school.sptech.KentoCafe;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import org.springframework.transaction.annotation.Transactional;
import school.sptech.KentoCafe.repository.PedidoRepository;

@SpringBootTest
public class CacheConfigTest {

    @Autowired
    private RedisCacheConfiguration redisCacheConfiguration;

    @Autowired
    private PedidoRepository pedidoRepository;



    @Test
    @Transactional
    void testarSerializacaoPedido() {

        var pedido = pedidoRepository.findAll().get(0);

        var serializer = redisCacheConfiguration
                .getValueSerializationPair()
                .getWriter();

        var buffer = serializer.write(pedido);

        byte[] json = new byte[buffer.remaining()];
        buffer.get(json);

        System.out.println(
                new String(json, StandardCharsets.UTF_8)
        );
    }
}
