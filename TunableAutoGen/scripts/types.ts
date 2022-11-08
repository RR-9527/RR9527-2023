export type MethodHeader = {
  name: string;
  params: Param[];
}

export type Param = {
  name: string;
  type: string;
  optional?: boolean;
  is_lambda?: boolean;
}
