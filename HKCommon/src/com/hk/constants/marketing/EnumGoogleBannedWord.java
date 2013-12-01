package com.hk.constants.marketing;

import com.hk.domain.marketing.GoogleBannedWord;


/**
 * Generated
 */
public enum EnumGoogleBannedWord {

  hgh("hgh",10L),
  human_growth_hormones("human growth hormones",20L),
  hiv_home_test("hiv home test",30L),
  dhea("dhea",40L),
  melatonin("melatonin",50L),
  hcg("hcg",60L),
  human_chorionic_gonadotropin("human chorionic gonadotropin",70L),
  steroid("steroid",80L),
  anabol("anabol",90L),
  cellmass("cellmass",100L),
  clenbuterol("clenbuterol",110L),
  dianabol("dianabol",120L),
  extreme_mass("extreme mass",130L),
  finakits("finakits",140L),
  mesterolone("mesterolone",150L),
  nitrix("nitrix",160L),
  novedex_xt("novedex xt",170L),
  tz("tz",180L),
  stack("stack",190L),
  vasospan("vasospan",200L),
  ephedra("ephedra",210L),
  ephedrine("ephedrine",220L),
  miracle_cure("miracle cures",230L),
  rx("rx",240L),
  glutamine("glutamine",250L)
  ;

  private java.lang.String bannedWord;
  private java.lang.Long id;

  EnumGoogleBannedWord(java.lang.String bannedWord, java.lang.Long id) {
    this.bannedWord = bannedWord;
    this.id = id;
  }

  public java.lang.String getBannedWord() {
    return bannedWord;
  }

  public java.lang.Long getId() {
    return id;
  }

    public GoogleBannedWord asGoogleBannedWord() {
    GoogleBannedWord googleBannedWord = new GoogleBannedWord();
    googleBannedWord.setId(this.getId());
    googleBannedWord.setBannedWord(this.getBannedWord());
    return googleBannedWord;
  }

}

