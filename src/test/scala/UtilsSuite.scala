import org.scalatest.FunSuite

class UtilsSuite extends FunSuite{
  val strGoodDate = "2015-11-28T15:40:50+00:00"
  val goodDate = Conversion.toDate(strGoodDate)
  val strLaterDate = "2015-11-29T15:40:50+00:00"
  val laterDate = Conversion.toDate(strLaterDate)

  test("the sanitize function works fine"){
    val goodLine = "id1," + strGoodDate + ",5,10,480,320"
    assert(Utils.sanitizeLine(goodLine) == SanitizedLine(Some("id1"), goodDate, Some(5), Some(10), Some(320), Some(480)))
    //Incomplete lines
    val badLine1 = ""
    assert(Utils.sanitizeLine(badLine1) == SanitizedLine(None, None, None, None, None, None))
    val badLine2 = "id1"
    assert(Utils.sanitizeLine(badLine2) == SanitizedLine(Some("id1"), None, None, None, None, None))
    val badLine3 = "id1,2015-11-28T15:40:50+00:00"
    assert(Utils.sanitizeLine(badLine3) == SanitizedLine(Some("id1"), goodDate, None, None, None, None))
    val badLine4 = "id1,2015-11-28T15:40:50+00:00,5"
    assert(Utils.sanitizeLine(badLine4) == SanitizedLine(Some("id1"), goodDate, Some(5), None, None, None))
    val badLine5 = "id1,2015-11-28T15:40:50+00:00,5,10"
    assert(Utils.sanitizeLine(badLine5) == SanitizedLine(Some("id1"), goodDate, Some(5), Some(10), None, None))
    val badLine6 = "id1,2015-11-28T15:40:50+00:00,5,10,480"
    assert(Utils.sanitizeLine(badLine6) == SanitizedLine(Some("id1"), goodDate, Some(5), Some(10), None, Some(480)))
    //Bad format
    val badFormatLine1 = "id1,aaaaaa"
    assert(Utils.sanitizeLine(badFormatLine1) == SanitizedLine(Some("id1"), None, None, None, None, None))
    val badFormatLine2 = "id1,2015-11-28T15:40:50+00:00,aaaaaa"
    assert(Utils.sanitizeLine(badFormatLine2) == SanitizedLine(Some("id1"), goodDate, None, None, None, None))
    val badFormatLine3 = "id1,2015-11-28T15:40:50+00:00,1,aaaaa"
    assert(Utils.sanitizeLine(badFormatLine3) == SanitizedLine(Some("id1"), goodDate, Some(1), None, None, None))
    val badFormatLine4 = "id1,2015-11-28T15:40:50+00:00,1,1,aaaaa"
    assert(Utils.sanitizeLine(badFormatLine4) == SanitizedLine(Some("id1"), goodDate, Some(1), Some(1), None, None))
    val badFormatLine5 = "id1,2015-11-28T15:40:50+00:00,1,1,1,aaaaa"
    assert(Utils.sanitizeLine(badFormatLine5) == SanitizedLine(Some("id1"), goodDate, Some(1), Some(1), None, Some(1)))
    //Empty line
    val emptyLine1 = ",,,,,"
    assert(Utils.sanitizeLine(emptyLine1) == SanitizedLine(None, None, None, None, None, None))
    val emptyLine2 = ",,,,,,,,,,,,"
    assert(Utils.sanitizeLine(emptyLine2) == SanitizedLine(None, None, None, None, None, None))
  }

  test("the statsReducer function works fine"){
    val stats1 = Stats(1,1,1,"id1",goodDate.get)
    val stats2 = Stats(2,2,2,"id2",laterDate.get)
    assert(Utils.statsReducer(stats1, stats2) == Stats(3,3,3,"id1",goodDate.get))
    val stats3 = Stats(1,0,1,"id1",laterDate.get)
    val stats4 = Stats(0,1,0,"id2",goodDate.get)
    assert(Utils.statsReducer(stats3, stats4) == Stats(1,1,1,"id2",goodDate.get))
  }

  test("the sanitizedLineReducer function works fine"){
    //No more 640x960 users
    val stats1 = Stats(85,5,100,"id1",goodDate.get)
    val line1 = SanitizedLine(Some("id2"), laterDate, Some(50), Some(1), Some(1), Some(1))
    assert(Utils.sanitizedLineReducer(stats1,line1) == Stats(86,5,150,"id1",goodDate.get))
    //One more 960x640 user
    val stats2 = Stats(85,5,100,"id1",goodDate.get)
    val line2 = SanitizedLine(Some("id2"), laterDate, Some(50), Some(1), Some(640), Some(960))
    assert(Utils.sanitizedLineReducer(stats2,line2) == Stats(86,6,150,"id1",goodDate.get))
    //Previous date
    val str2011Date = "2011-11-28T15:40:50+00:00"
    val date2011 = Conversion.toDate(str2011Date)
    val stats3 = Stats(85,5,100,"id1",goodDate.get)
    val line3 = SanitizedLine(Some("id2"), date2011, Some(50), Some(1), Some(1), Some(1))
    assert(Utils.sanitizedLineReducer(stats3,line3) == Stats(86,5,150,"id2",date2011.get))
  }
}
