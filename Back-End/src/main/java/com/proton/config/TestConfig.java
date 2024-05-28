package com.proton.config;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.proton.models.entities.assunto.Assunto;
import com.proton.models.entities.endereco.Endereco;
import com.proton.models.entities.municipe.Municipe;
import com.proton.models.entities.protocolo.Devolutiva;
import com.proton.models.entities.protocolo.Protocolo;
import com.proton.models.entities.secretaria.Secretaria;
import com.proton.models.enums.Role;
import com.proton.models.enums.Status;
import com.proton.models.enums.StatusRedirecionamento;
import com.proton.models.redirecionamento.Redirecionamento;
import com.proton.models.repositories.AssuntoRepository;
import com.proton.models.repositories.DevolutivaRepository;
import com.proton.models.repositories.EnderecoRepository;
import com.proton.models.repositories.MunicipeRepository;
import com.proton.models.repositories.ProtocoloRepository;
import com.proton.models.repositories.RedirecionamentoRepository;
import com.proton.models.repositories.SecretariaRepository;
import com.proton.services.GeradorCPF;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

        @Autowired
        private EnderecoRepository enderecoRepository;

        @Autowired
        private MunicipeRepository municipeRepository;

        @Autowired
        private SecretariaRepository secretariaRepository;

        @Autowired
        private ProtocoloRepository protocoloRepository;

        @Autowired
        private AssuntoRepository assuntoRepository;

        @Autowired
        private DevolutivaRepository devolutivaRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private RedirecionamentoRepository redirecionamentoRepository;

        @Override
        public void run(String... args) throws Exception {
                Endereco end1 = new Endereco(null, "apartamento", "54321-876", "Avenida Secundária", "Casa 202",
                                "456", "Edifício B", "Bairro Novo", "Rio de Janeiro", "RJ", "Brasil");

                Endereco end2 = new Endereco(null, "escritório", "98765-432", "Praça Central", "Sala 301",
                                "789", "Torre C", "Centro Histórico", "Porto Alegre", "RS", "Brasil");

                Endereco end3 = new Endereco(null, "casa", "11111-222", "Rua dos Fundos", "Casa 303",
                                "101", "Bloco C", "Periferia", "Belo Horizonte", "MG", "Brasil");
                Endereco end4 = new Endereco(null, "casa", "11111-222", "Rua dos Fundos", "Casa 303",
                                "101", "Bloco C", "Periferia", "Belo Horizonte", "MG", "Brasil");
                Endereco end5 = new Endereco(null, "casa", "11111-222", "Rua dos Fundos", "Casa 303",
                                "101", "Bloco C", "Periferia", "Belo Horizonte", "MG", "Brasil");

                Endereco end6 = new Endereco(null, "pipipipopopo", "11111-222", "Rua dos Fundos", "Casa 303",
                                "101", "Bloco C", "Periferia", "Belo Horizonte", "MG", "Brasil");
                Secretaria sec1 = new Secretaria(null, "Secretaria de Educação", "Ana Silva", "ana@example.com",
                                "senha123",
                                end2);

                Secretaria sec2 = new Secretaria(null, "Secretaria de Saúde", "Carlos Santos", "carlos@example.com",
                                "senha456",
                                end3);

                Secretaria sec3 = new Secretaria(null, "Secretaria de Meio Ambiente", "Mariana Oliveira",
                                "mariana@example.com",
                                "senha789", end1);

                String senha = passwordEncoder.encode("123456");

                Municipe mun1 = new Municipe("Fulano", "fulano@example.com", senha, "973.087.140-04",
                                "(11)96256-8965",
                                LocalDate.of(1990, 5, 15), end3);
                mun1.setRole(Role.MUNICIPE);

                Municipe mun2 = new Municipe("Ciclano", "ciclano@example.com", senha, "699.367.750-40",
                                "(11)96256-8965",
                                LocalDate.of(1985, 10, 25), end2);
                mun2.setRole(Role.MUNICIPE);

                Municipe fun1 = new Municipe("Secretário", "secretario@email.com", senha, "999.654.321-00",
                                "(11)96256-8965",
                                LocalDate.of(1985, 10, 25), end4);
                fun1.setRole(Role.SECRETARIO);

                Municipe fun2 = new Municipe("Coordenador", "coordenador@email.com", senha, "818.194.010-57",
                                "(11)96256-8965",
                                LocalDate.of(1985, 10, 25), end5);
                fun2.setRole(Role.COORDENADOR);

                Municipe fun3 = new Municipe("Funcionário", "funcionario@email.com", senha, "564.278.220-71",
                                "(11)96256-8965",
                                LocalDate.of(1985, 10, 25), end1);
                fun3.setRole(Role.FUNCIONARIO);

                Protocolo prot1 = new Protocolo(null, sec1, mun1, end2, "Assunto do protocolo", new Date(),
                                "Descrição do protocolo", Status.CIENCIA, 100.0, "001-2024");
                               

                Protocolo prot2 = new Protocolo(null, sec2, mun2, end3, "Outro assunto", new Date(), "Outra descrição",
                                Status.PAGAMENTO_PENDENTE,
                                150.0, "002-2024");

                Protocolo prot3 = new Protocolo(null, sec2, mun2, end3, "Teste", new Date(), "teste", Status.CONCLUIDO,
                                150.0, "003-2024");

                Protocolo prot4 = new Protocolo(null, sec1, mun1, end2, "Assunto do protocolo", new Date(),
                "Descrição do protocolo", Status.CIENCIA, 100.0, "004-2024");  

                Protocolo prot5 = new Protocolo(null, sec1, mun1, end2, "Assunto do protocolo", new Date(),
                "Descrição do protocolo", Status.CIENCIA, 100.0, "005-2024");

                Protocolo prot6 = new Protocolo(null, sec1, mun1, end2, "Assunto do protocolo", new Date(),
                "Descrição do protocolo", Status.CIENCIA, 100.0, "006-2024");

                Assunto assunto1 = new Assunto(1, "Problema de iluminação pública", sec1, 130.50);

                Assunto assunto2 = new Assunto(2, "Problema de coleta de lixo", sec2, 150.55);

                Assunto assunto3 = new Assunto(3, "Problema de trânsito", sec3, 30.00);


                Devolutiva dev1 = new Devolutiva(null, null, prot1, Instant.now(), "Teste");
                // Manda para o banco de dados
                municipeRepository.saveAll(Arrays.asList(mun1, mun2, fun1, fun2, fun3));
                enderecoRepository.saveAll(Arrays.asList(end1, end2, end3, end6));
                secretariaRepository.saveAll(Arrays.asList(sec1, sec2, sec3));
                protocoloRepository.saveAll(Arrays.asList(prot1, prot2, prot3, prot4, prot5, prot6));
                assuntoRepository.saveAll((Arrays.asList(assunto1, assunto2, assunto3)));
                devolutivaRepository.save(dev1);
        }
}
