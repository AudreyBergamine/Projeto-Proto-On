//SERVICE LAYER (RESOURCER -->SERVICE LAYER(AQUI) --> REPOSITORY
package com.proton.services.protocolo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.proton.models.entities.Log;
import com.proton.models.entities.endereco.Endereco;
import com.proton.models.entities.municipe.Municipe;
import com.proton.models.entities.protocolo.Protocolo;
import com.proton.models.entities.secretaria.Secretaria;
import com.proton.models.repositories.EnderecoRepository;
import com.proton.models.repositories.LogRepository;
import com.proton.models.repositories.MunicipeRepository;
import com.proton.models.repositories.ProtocoloRepository;
import com.proton.models.repositories.SecretariaRepository;
import com.proton.services.exceptions.ResourceNotFoundException;

import com.proton.services.municipe.MunicipeService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProtocoloService {

	@Autowired
	private ProtocoloRepository protocoloRepository;

	@Autowired
	private MunicipeService municipeService;

	@Autowired
	private MunicipeRepository municipeRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private SecretariaRepository secretariaRepository;

	@Autowired
	private LogRepository logRepository;

	private final JdbcTemplate jdbcTemplate; // Para fazer consultas no sql
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	Log log = new Log();

	@Autowired
	public ProtocoloService(JdbcTemplate jdbcTemplate) { // Construtor para o Spring injetar o jdbcTemplate no Protocolo
		this.jdbcTemplate = jdbcTemplate;
	}

	// Método para encontrar TODOS os protocolos
	public List<Protocolo> findAll() {
		return protocoloRepository.findAll();
	}

	// Método para encontrar protocolo pelo id
	public Protocolo findById(Integer id) {
		Optional<Protocolo> obj = protocoloRepository.findById(id);
		return obj.get();
	}

	// Método para encontrar protocolo pelo numero do protocolo
	public Protocolo findByNumero_protocolo(String numero_protocolo) {
		Optional<Protocolo> obj = protocoloRepository.findByNumeroProtocolo(numero_protocolo);
		return obj.get();
	}

	public Protocolo insert(Protocolo protocolo, Integer id_m, Long id_s) {
		Municipe mun = municipeRepository.getReferenceById(id_m);
		Secretaria sec = secretariaRepository.getReferenceById(id_s);
		Endereco end = enderecoRepository.getReferenceById(mun.getEndereco().getId_endereco());
		String numeroProtocolo = this.gerarNumeroProtocolo();
		protocolo.setNumero_protocolo(numeroProtocolo);
		protocolo.setMunicipe(mun);
		protocolo.setEndereco(end);
		protocolo.setSecretaria(sec);		

		return protocoloRepository.save(protocolo);
	}

	// Método para encontrar TODOS protocolos do MUNICIPE
	public List<Protocolo> findByMunicipe(Municipe municipe) {
		return protocoloRepository.findAllByMunicipe(municipe);
	}

	public List<Protocolo> findByNomeMunicipe(String nomeMunicipe) {
		Municipe municipe = municipeService.findByNome(nomeMunicipe);
		Integer idMunicipe = municipe.getId();
		return protocoloRepository.findByMunicipe(idMunicipe);
	}

	private void updateData(Protocolo entity, Protocolo obj) {
		entity.setSecretaria(obj.getSecretaria());
		entity.setMunicipe(obj.getMunicipe());
		entity.setEndereco(obj.getEndereco());
		entity.setAssunto(obj.getAssunto());
		entity.setDescricao(obj.getDescricao());
		entity.setValor(obj.getValor());
		entity.setStatus(obj.getStatus());
	}

	public Protocolo updateStatus(String numeroProtocolo, Protocolo status, String Nomefuncionario) {
		try {
			Protocolo entity = protocoloRepository.findByNumeroProtocolo(numeroProtocolo)
					.orElseThrow(() -> new RuntimeException("Protocolo não encontrado"));

			String mensagemLog = String.format("%s alterou status do protocolo " + entity.getNumero_protocolo() + " de %s para %s em %s",
			Nomefuncionario, entity.getStatus(), status.getStatus(), LocalDateTime.now().format(formatter));

			updateData(entity, status);

			log.setMensagem(mensagemLog);
			logRepository.save(log);

			return protocoloRepository.save(entity);
		} catch (EntityNotFoundException e) { //
			throw new ResourceNotFoundException(numeroProtocolo);
		}
	}

	public Protocolo updateRedirect(String numeroProtocolo, Protocolo secretaria, String Nomefuncionario) {
		try {
			Protocolo entity = protocoloRepository.findByNumeroProtocolo(numeroProtocolo)
					.orElseThrow(() -> new RuntimeException("Protocolo não encontrado"));

			String mensagemLog = String.format("%s redirecionou o protocolo " + entity.getNumero_protocolo() + " de %s para %s em %s",
			Nomefuncionario, entity.getSecretaria().getNome_secretaria(), secretaria.getSecretaria().getNome_secretaria(),
					LocalDateTime.now().format(formatter));

			updateData(entity, secretaria);

			Log log = new Log();
			log.setMensagem(mensagemLog);
			logRepository.save(log);

			return protocoloRepository.save(entity);
		} catch (EntityNotFoundException e) { //
			throw new ResourceNotFoundException(numeroProtocolo);
		}
	}

	@SuppressWarnings("unused") // Serve para parar de aportar o um erro especifico ksksks, mas nem é erro.
	public String gerarNumeroProtocolo() {
		String anoAtual = String.valueOf(LocalDate.now().getYear());
		String sql = "SELECT MAX(SUBSTRING(numero_protocolo, 1, POSITION('-' IN numero_protocolo) - 1)) FROM Protocolo WHERE numero_protocolo LIKE ?";
		Integer ultimoNumero = jdbcTemplate.queryForObject(sql, Integer.class, "%-" + anoAtual);
		if (ultimoNumero == null) {
			return "001-" + anoAtual;
		} else {
			int proximoNumeroProtocolo = ultimoNumero + 1;
			String novoNumeroProtocolo = String.format("%03d", proximoNumeroProtocolo);

			return novoNumeroProtocolo + "-" + anoAtual;
		}
	}

}