package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdentifiantSequenceTest {

    @Test
    void lesSequencesEtudiantsEtBauxSontIndependantesEtSeReinitialisentParInstance() {
        IdentifiantSequence premiere = new IdentifiantSequence();

        assertEquals("ETU-1", premiere.prochainEtudiant());
        assertEquals("ETU-2", premiere.prochainEtudiant());
        assertEquals("BAIL-001", premiere.prochainBail());
        assertEquals("BAIL-002", premiere.prochainBail());

        IdentifiantSequence reinitialisee = new IdentifiantSequence();
        assertEquals("ETU-1", reinitialisee.prochainEtudiant());
        assertEquals("BAIL-001", reinitialisee.prochainBail());
    }
}
