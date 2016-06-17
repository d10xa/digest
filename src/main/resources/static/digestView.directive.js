(function() {
    'use strict';

    angular
        .module('app')
        .directive('djtDigestView', djtDigestView);

    /* @ngInject */
    function djtDigestView() {
        var directive = {
            restrict: 'EA',
            templateUrl: '/digest-view.html',
            link: linkFunc,
            controller: DigestViewCtrl,
            controllerAs: 'vm',
            bindToController: {
              task: '='
            }
        };

        return directive;

        function linkFunc(scope, el, attr, ctrl) {

        }
    }

    DigestViewCtrl.$inject = ['Digest'];

    /* @ngInject */
    function DigestViewCtrl(Digest) {
        var vm = this;

        activate();

        function activate() {
            vm.alertClass = {
               'alert-success': vm.task.status == "SUCCESS",
               'alert-info': vm.task.status == "IN_PROCESS" || vm.task.status == "NEW",
               'alert-warning': vm.task.status == "INTERRUPTED",
               'alert-danger': vm.task.status == "EXCEPTIONAL",
            };
        }
    }
})();
