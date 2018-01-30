import org.scalatest.FunSuite

class ConversionSuite extends FunSuite{

  test("dateFormat function works fine"){
    val strDate = "2015-11-25T01:29:14+00:00"
    val convertedDate = Conversion.toDate(strDate)
    assert(convertedDate.get.toString == "Wed Nov 25 01:29:14 GMT 2015")
    val strDate2 = "2015-11-29T11:11:49+00:00"
    val convertedDate2 = Conversion.toDate(strDate2)
    assert(convertedDate2.get.toString == "Sun Nov 29 11:11:49 GMT 2015")
    val strDate3 = "2015-11-25T02:02:40+00:00"
    val convertedDate3 = Conversion.toDate(strDate3)
    assert(convertedDate3.get.toString == "Wed Nov 25 02:02:40 GMT 2015")
    val strDate4 = "2015-11-29T05:51:15+00:00"
    val convertedDate4 = Conversion.toDate(strDate4)
    assert(convertedDate4.get.toString == "Sun Nov 29 05:51:15 GMT 2015")
  }

  test("check date comparation"){
    val strDate = "2015-11-25T01:29:14+00:00"
    val convertedDate = Conversion.toDate(strDate)
    val strDate2 = "2015-11-29T11:11:49+00:00"
    val convertedDate2 = Conversion.toDate(strDate2)
    assert(convertedDate.get.before(convertedDate2.get) == true)
  }
}
