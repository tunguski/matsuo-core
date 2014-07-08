describe("backbone", function() {
  it("lastUrlElement() works", function() {
    expect(lastUrlElement(function (str) {
      return '/test/xxx/7';
    })).toBe('7');
  });

  var obj = {
    date: moment(1397426400000).toDate(),
    str: 'test value'
  };

  it("paramsObject() returns serialized date", function () {
    // result contains proper value
    expect(paramsObject(obj).date).toBe('2014-04-13T22:00:00.000Z');
    // source object was not modified
    expect(angular.isDate(obj.date)).toBe(true);
  });

  it("params() returns serialized date", function () {
    expect(params(obj)).toBe('date=2014-04-13T22:00:00.000Z&str=test value&');
  });
});

