package com.example.asd.getCode


import com.google.gson.annotations.SerializedName

data class Row(
    @SerializedName("ATPT_OFCDC_SC_CODE")
    var aTPTOFCDCSCCODE: String?,
    @SerializedName("ATPT_OFCDC_SC_NM")
    var aTPTOFCDCSCNM: String?,
    @SerializedName("COEDU_SC_NM")
    var cOEDUSCNM: String?,
    @SerializedName("DGHT_SC_NM")
    var dGHTSCNM: String?,
    @SerializedName("ENE_BFE_SEHF_SC_NM")
    var eNEBFESEHFSCNM: String?,
    @SerializedName("ENG_SCHUL_NM")
    var eNGSCHULNM: String?,
    @SerializedName("FOAS_MEMRD")
    var fOASMEMRD: String?,
    @SerializedName("FOND_SC_NM")
    var fONDSCNM: String?,
    @SerializedName("FOND_YMD")
    var fONDYMD: String?,
    @SerializedName("HMPG_ADRES")
    var hMPGADRES: String?,
    @SerializedName("HS_GNRL_BUSNS_SC_NM")
    var hSGNRLBUSNSSCNM: String?,
    @SerializedName("HS_SC_NM")
    var hSSCNM: Any?,
    @SerializedName("INDST_SPECL_CCCCL_EXST_YN")
    var iNDSTSPECLCCCCLEXSTYN: String?,
    @SerializedName("JU_ORG_NM")
    var jUORGNM: String?,
    @SerializedName("LCTN_SC_NM")
    var lCTNSCNM: String?,
    @SerializedName("LOAD_DTM")
    var lOADDTM: String?,
    @SerializedName("ORG_FAXNO")
    var oRGFAXNO: String?,
    @SerializedName("ORG_RDNDA")
    var oRGRDNDA: String?,
    @SerializedName("ORG_RDNMA")
    var oRGRDNMA: String?,
    @SerializedName("ORG_RDNZC")
    var oRGRDNZC: String?,
    @SerializedName("ORG_TELNO")
    var oRGTELNO: String?,
    @SerializedName("SCHUL_KND_SC_NM")
    var sCHULKNDSCNM: String?,
    @SerializedName("SCHUL_NM")
    var sCHULNM: String?,
    @SerializedName("SD_SCHUL_CODE")
    var sDSCHULCODE: String?,
    @SerializedName("SPCLY_PURPS_HS_ORD_NM")
    var sPCLYPURPSHSORDNM: Any?
)