package br.senai.sp.gestaoAuditorio.util;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;


public class HashUtil {

	public static String hash256(String palavra) {

		// tempero para o hash
		String salt = "Open@sezame";

		// acescenta o tempero
		palavra = palavra + salt;

		// criando o hash e armazando na string sha

		String sha256 = Hashing.sha256().hashString(palavra, StandardCharsets.UTF_8).toString();

		return sha256;

	}
}
