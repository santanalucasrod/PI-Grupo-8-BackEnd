package school.sptech.KentoCafe.service.s3;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import school.sptech.KentoCafe.dto.imagem.ImagemValidada;

@Service
public class ValidadorImagemService {

    private static final long TAMANHO_MAXIMO = 5L * 1024 * 1024;
    private static final int DIMENSAO_MAXIMA = 6000;
    private static final long PIXELS_MAXIMOS = 12_000_000L;

    public ImagemValidada validar(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Selecione uma imagem.");
        }

        if (arquivo.getSize() > TAMANHO_MAXIMO) {
            throw new ResponseStatusException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "A imagem deve ter no máximo 5 MB.");
        }

        final byte[] bytes;
        try {
            bytes = arquivo.getBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Não foi possível ler o arquivo.", e);
        }

        try (ImageInputStream input = new MemoryCacheImageInputStream(
                new ByteArrayInputStream(bytes))) {

            Iterator<ImageReader> leitores = ImageIO.getImageReaders(input);
            if (!leitores.hasNext()) {
                throw new ResponseStatusException(
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        "Formato não permitido. Use JPEG ou PNG.");
            }

            ImageReader leitor = leitores.next();
            try {
                leitor.setInput(input, true, true);

                int largura = leitor.getWidth(0);
                int altura = leitor.getHeight(0);
                if (largura <= 0 || altura <= 0
                        || largura > DIMENSAO_MAXIMA
                        || altura > DIMENSAO_MAXIMA
                        || (long) largura * altura > PIXELS_MAXIMOS) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "As dimensões da imagem excedem os limites permitidos.");
                }

                String formato = leitor.getFormatName().toLowerCase(Locale.ROOT);
                String contentType;
                String extensao;

                if (formato.equals("jpg") || formato.equals("jpeg")) {
                    contentType = "image/jpeg";
                    extensao = "jpg";
                } else if (formato.equals("png")) {
                    contentType = "image/png";
                    extensao = "png";
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                            "Formato não permitido. Use JPEG ou PNG.");
                }

                BufferedImage imagem = leitor.read(0);
                if (imagem == null) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "A imagem está inválida ou corrompida.");
                }

                byte[] normalizado;
                try (ByteArrayOutputStream saida = new ByteArrayOutputStream()) {
                    String formatoSaida = extensao.equals("jpg") ? "jpeg" : "png";
                    boolean gravou = ImageIO.write(imagem, formatoSaida, saida);
                    if (!gravou) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Não foi possível normalizar a imagem.");
                    }
                    normalizado = saida.toByteArray();
                } finally {
                    imagem.flush();
                }

                if (normalizado.length > TAMANHO_MAXIMO) {
                    throw new ResponseStatusException(
                            HttpStatus.PAYLOAD_TOO_LARGE,
                            "A imagem processada excede o limite de 5 MB.");
                }

                return new ImagemValidada(normalizado, contentType, extensao);
            } finally {
                leitor.dispose();
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IOException | RuntimeException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Não foi possível validar o conteúdo da imagem.", e);
        }
    }
}
