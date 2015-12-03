{ Expression } = require './expression'
{ build } = require './builder'
{Quantity} = require('./quantity')
class Element
  constructor: (json) ->
    @name = json.name
    @value = build json.value
  exec: (ctx) ->
    @value?.exec(ctx)


module.exports.Instance = class Instance extends Expression
  constructor: (json) ->
    super
    @classType = json.classType
    @element = ( new Element(child) for child in json.element)
 
  # TODO:  Implement switch statement for other system types such as Code and Concept
  # 
  exec: (ctx) ->
    obj = {}
    for el in @element
      obj[el.name] = el.exec(ctx)
    # TODO: Support for other classes like Code and Concept
    if @classType is "{urn:hl7-org:elm-types:r1}Quantity" then new Quantity(obj) else obj
