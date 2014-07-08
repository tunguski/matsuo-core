describe('Modified translation module', function () {


  describe('validationService', function () {
    var _translate;
    beforeEach(inject(function ($translate) {
      _translate = $translate;
    }));


    it('should show field errors', function () {
      expect(_translate.instant('value.first')).toBe('one');
      expect(_translate.instant('value.second')).toBe('two');
    });
  });
});

