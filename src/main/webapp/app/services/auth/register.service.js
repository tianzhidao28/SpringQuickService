(function () {
    'use strict';

    angular
        .module('springQuickServiceApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
